package com.example.quily.services;

import com.example.quily.exception.GreaterIndicesFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.OriginalLinkResponse;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Optional;

@Service
@Component
public class ShortLinkServiceImpl implements DbService<ShortLink, Long> {
	@Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private KeyGeneratorService KeyGeneratorService;

    @Override
    public Flux<ShortLink> findAll() {
        return null;
    }

    @Override
    public Mono<ShortLink> findByUniqueId(Long id) {
        return null;
    }

    @Override
    public Mono<ShortLink> update(ShortLink shortLink) {
        return null;
    }

    @Override
    public void delete(Long id) {}

    @Override
    public Mono<ShortLink> save(ShortLink shortLink) throws RuntimeException {
        String sql = "INSERT INTO short_link (user_id, original_link, shorted_link, creation_date, expiry_date) " +
                "SELECT :user_id, :original_link, :shorted_link, :creation_date, :expiry_date " +
                "FROM dual WHERE NOT EXISTS (" +
                "   SELECT 1 FROM short_link WHERE user_id = :user_id AND original_link Like  :compare_link" +
                ")";
        return databaseClient.sql(sql)
                .bind("user_id", shortLink.getUserID())
                .bind("compare_link", shortLink.getOriginalLinkWithoutParams())
                .bind("original_link", shortLink.getOriginalLink())
                .bind("shorted_link", shortLink.getShortedLink())
                .bind("creation_date", shortLink.getCreationDate())
                .bind("expiry_date", shortLink.getExpiryDate())
                .fetch()
                .rowsUpdated()
                .flatMap(row -> {
                    if (row > 0)
                        return Mono.just(shortLink);
                    else
                        return Mono.error(new GreaterIndicesFoundException("Provided link already has been used."));
                });
    }

	public Mono<OriginalLinkResponse> findOriginalLink(String shortLink) {
		return databaseClient.sql("SELECT original_link FROM short_link WHERE shorted_link = :shortLink")
				.bind("shortLink", shortLink)
				.fetch()
				.first()
				.map(row -> new OriginalLinkResponse((String) row.get("original_link")));
	}

    public Mono<Boolean> hasCustomAliasBeenUsed(CreateShortLinkRequest createShortLinkRequest){
        return databaseClient.sql("SELECT shorted_link FROM short_link WHERE shorted_link = :customAlias")
                .bind("customAlias", ShortLinkUtil.localBaseUrl + createShortLinkRequest.getCustomAlias())
                .fetch()
                .first().map(customAlias -> true)
                .switchIfEmpty(Mono.just(false));
    }

    public Mono<ShortLink> createSortLinkAndUpdateIndices(ShortLink shortLink,  Optional<KeyIndices> currentIndicesOpt)
    {   Mono<ShortLink> link = save(shortLink);
        Mono<KeyIndices> indices = currentIndicesOpt.map(KeyGeneratorService::updateIfGreater).orElse(Mono.empty());

        return indices.flatMap(ind -> Mono.zip(link, Mono.just(ind)).map(Tuple2::getT1))
                .switchIfEmpty(link);
    }
}

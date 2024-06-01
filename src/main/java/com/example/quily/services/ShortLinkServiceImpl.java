package com.example.quily.services;

import com.example.quily.exception.GreaterIndicesFoundException;
import com.example.quily.model.ShortLink;
import com.example.quily.repositories.ShortLinkRepository;
import com.example.quily.response.OriginalLinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Component
public class ShortLinkServiceImpl implements DbService<ShortLink, Long> {
    private final DatabaseClient databaseClient;

    public ShortLinkServiceImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

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
}

package com.example.quily.services;

import com.example.quily.exception.AlreadyExistEntityException;
import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.model.User;
import com.example.quily.repositories.UserRepository;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.quily.dao.EmailConfirmationTokenDAO;

@Service
public class UserService implements DbService<User, String>{
    @Autowired
    private UserRepository userRepository;

    private final DatabaseClient databaseClient;

    @Autowired
    EmailTokenService emailTokenService;

    @Autowired
    EmailService emailService;

    public UserService(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<User> findAll() {
        return null;
    }

    @Override
    public Mono<User> findByUniqueId(String email) {
        return userRepository.findById(email);
    }

    @Override
    public Mono<User> save(User user) throws RuntimeException {;
        return userRepository.findById(user.getEmail())
                .flatMap(dbUser -> {
                    if (dbUser.isActive())
                       return Mono.error(new AlreadyExistEntityException("User with email " + user.getEmail() + " already exist"));
                    else return Mono.just(dbUser);
                })
                .switchIfEmpty(
                        databaseClient.sql("INSERT INTO users (email, user_name, password, is_active) VALUES (:email, :name, :password, :is_active)")
                                .bind("email", user.getEmail())
                                .bind("name", user.getUserName())
                                .bind("password", user.getPassword())
                                .bind("is_active", user.isActive())
                                .fetch()
                                .rowsUpdated()
                                .thenReturn(user)
                );
    }

    @Override
    public Mono<User> update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(String email) {
        userRepository.deleteById(email);
    }

    public Mono<Long> makeUserActive(String email) {
        return databaseClient.sql("UPDATE users SET is_active = 1 WHERE email = :email")
                .bind("email", email)
                .fetch()
                .rowsUpdated();
    }

    public Mono<String> sendEmailVerificationLink (User user) {
        String token = EmailConfirmationTokenDAO.getConfirmationToken(user);
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, CommonUtil.getCurrentDateTimeInFormat(), user.getEmail());

        Mono<EmailConfirmationToken> saveToken = emailTokenService.save(confirmationToken);
        Mono<Void> sentEmail = Mono.fromRunnable(() -> {
            emailService.sendEmail(user, token);
        });
        Mono<User> savedUser = save(user);

        return Mono.when(saveToken, sentEmail, savedUser)
                .then(Mono.fromCallable(() -> "Email verification link sent on email " + user.getEmail() + " please verify it to click it" +
                        " if you didn't get verification link please check your email"));
    }

    public Mono<String> verifyTokenAndSaveUser(String token) {
        return emailTokenService.findByToken(token)
                .flatMap(confirmedToken -> makeUserActive(confirmedToken.getUserEmail())
						.flatMap(activatedUser -> emailTokenService.deleteByToken(token)
                                .then(Mono.fromCallable(() -> "Email verified successfully"))))
                .switchIfEmpty(Mono.just("Email verification failed, token did not match"));
    }
}

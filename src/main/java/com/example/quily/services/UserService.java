package com.example.quily.services;

import com.example.quily.constants.UserConstants;
import com.example.quily.dao.EmailConfirmationTokenDAO;
import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.exception.InternalServerError;
import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.User;
import com.example.quily.repositories.UserRepository;
import com.example.quily.response.SignUpResponse;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.ZonedDateTime;

import static com.example.quily.constants.CommonConstants.COMMON_INTERNAL_SERVER_MESSAGE;

@Service
public class UserService {
   private final UserRepository userRepository;
   private final EmailTokenService emailTokenService;
   private final EmailConfirmationTokenDAO emailConfirmationTokenDAO;
   private final PasswordEncoder passwordEncoder;
   private final KeyGeneratorService keyGeneratorService;

   @Autowired
   public UserService(UserRepository userRepository, EmailTokenService emailTokenService, EmailConfirmationTokenDAO emailConfirmationTokenDAO, PasswordEncoder passwordEncoder, KeyGeneratorService keyGeneratorService, KeyGeneratorDAO keyGeneratorDAO) {
      this.userRepository = userRepository;
      this.emailTokenService = emailTokenService;
      this.emailConfirmationTokenDAO = emailConfirmationTokenDAO;
      this.passwordEncoder = passwordEncoder;
      this.keyGeneratorService = keyGeneratorService;
   }

   private SignUpResponse createSignUpResponse(String email, boolean isVerificationLinkResent) {
      return new SignUpResponse(email, CommonUtil.getCurrentDateTimeInFormat(), isVerificationLinkResent);
   }

   private Mono<String> saveKeyIndicesAndSendEmail(String tokenKey, KeyIndices keyIndices, User user) {
      return keyGeneratorService
         .updateKeyIndices(keyIndices)
         .flatMap(updatedKeyIndices ->
            Mono.fromRunnable(() -> emailConfirmationTokenDAO.sendEmail(user, tokenKey))
               .then(Mono.fromCallable(() ->
                  String.format(UserConstants.EMAIL_VERIFICATION_MESSAGE, user.getEmail()))));
   }

   private Mono<SignUpResponse> processVerificationEmail(String token, EmailConfirmationToken emailConfirmationToken) {
      Mono<Long> activatedUser = userRepository.makeUserActive(emailConfirmationToken.getUserEmail());
      Mono<Long> deletedToken = emailTokenService.deleteByToken(token);

      return Mono.zip(activatedUser, deletedToken)
         .map(result -> createSignUpResponse(emailConfirmationToken.getUserEmail(), false))
         .switchIfEmpty(Mono.error(new InternalServerError(COMMON_INTERNAL_SERVER_MESSAGE)));
   }

   public Mono<User> createUser(User user) {
      return userRepository.findUserByEmail(user.getEmail())
         .flatMap(dbUser -> {
            if (dbUser.isActive())
               return Mono.error(new EntityAlreadyExistException("User with email " + user.getEmail() + " already exist"));
            else return Mono.just(dbUser);
         })
         .switchIfEmpty(userRepository.createUser(user));
      }

   public Mono<String> sendEmailVerificationLink(User user) {
      return createUser(user)
         .flatMap(signUpUser ->
            emailConfirmationTokenDAO.getConfirmationToken()
               .flatMap(token -> {
                  EmailConfirmationToken confirmationToken =
                     new EmailConfirmationToken(token.getKeyGeneratorResponse().getHashKey(), user.getEmail());
                  return emailTokenService.save(confirmationToken)
                     .flatMap(savedOrUpdatedToken ->
                        saveKeyIndicesAndSendEmail(token.getKeyGeneratorResponse().getHashKey(), token.getNextIndices(), user));
               })
         );
   }

   public Mono<SignUpResponse> verifyTokenAndSaveUser(String token) {
      return emailTokenService.findByToken(token)
         .flatMap(confirmedToken -> {
            Duration tokenAge = Duration.between(confirmedToken.getUpdateTime(), ZonedDateTime.now());

            if (tokenAge.toMinutes() > 5) {
               return userRepository.findUserByEmail(confirmedToken.getUserEmail())
                  .flatMap(this::sendEmailVerificationLink)
                  .map(result -> createSignUpResponse(confirmedToken.getUserEmail(), true));
            }
            return processVerificationEmail(token, confirmedToken);
         })
         .switchIfEmpty(Mono.error(new BadRequestException(UserConstants.VERIFICATION_FAILED_MESSAGE)));
   }

   public Mono<User> getUserByEmail(String email) {
      return userRepository.findUserByEmail(email);
   }

   public Mono<User> getUserAndCheckCredentials(String email, String password) {
      return getUserByEmail(email)
         .filter(user -> passwordEncoder.matches(password, user.getPassword()))
         .filter(User::isActive);
   }
}

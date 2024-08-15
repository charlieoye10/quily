package com.example.quily.services;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.constants.UserConstants;
import com.example.quily.dao.EmailConfirmationTokenDAO;
import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.exception.InternalServerError;
import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.User;
import com.example.quily.repositories.UserRepository;
import com.example.quily.request.ForgetPasswordRequest;
import com.example.quily.request.ResetPasswordRequest;
import com.example.quily.request.VerifyEmailForgetPasswordRequest;
import com.example.quily.response.SignUpResponse;
import com.example.quily.security.UserDetailsServiceImpl;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.ZonedDateTime;

import static com.example.quily.constants.CommonConstants.COMMON_INTERNAL_SERVER_MESSAGE;
import static com.example.quily.constants.CommonConstants.EMAIL_VERIFICATION_TOKEN_AGE;

@Service
public class UserService {
   private final UserRepository userRepository;
   private final EmailTokenService emailTokenService;
   private final EmailConfirmationTokenDAO emailConfirmationTokenDAO;
   private final PasswordEncoder passwordEncoder;
   private final KeyGeneratorService keyGeneratorService;
   private final UserDetailsServiceImpl userDetailsService;


   @Autowired
   public UserService(UserRepository userRepository,
                      EmailTokenService emailTokenService,
                      EmailConfirmationTokenDAO emailConfirmationTokenDAO,
                      PasswordEncoder passwordEncoder,
                      KeyGeneratorService keyGeneratorService,
                      UserDetailsServiceImpl userDetailsService) {
      this.userRepository = userRepository;
      this.emailTokenService = emailTokenService;
      this.emailConfirmationTokenDAO = emailConfirmationTokenDAO;
      this.passwordEncoder = passwordEncoder;
      this.keyGeneratorService = keyGeneratorService;
      this.userDetailsService = userDetailsService;

   }

   private SignUpResponse createSignUpResponse(String email, boolean isVerificationLinkResent) {
      return new SignUpResponse(email, CommonUtil.getCurrentDateTimeInFormat(), isVerificationLinkResent);
   }

   private Mono<String> saveKeyIndicesAndSendEmail(String tokenKey, KeyIndices keyIndices, User user, Boolean isRegistration) {
      Mono<KeyIndices> updatedKeyIndices = keyGeneratorService.updateKeyIndices(keyIndices);

      Mono<String> sentEmail = Mono.fromRunnable(() -> {
         if (isRegistration) {
            sendEmailForRegistrationVerification(user, tokenKey);
         } else {
            sendEmailForPasswordVerification(user, tokenKey);
         }
      }).then(Mono.fromCallable(() -> {
         String message = isRegistration
            ? UserConstants.EMAIL_VERIFICATION_MESSAGE
            : UserConstants.FORGOT_PASSWORD_VERIFICATION_MESSAGE;
         return String.format(message, user.getEmail());
      }));

      return Mono.zip(updatedKeyIndices, sentEmail)
         .map(Tuple2::getT2);
   }

   private Void sendEmailForRegistrationVerification(User user, String tokenKey) {
      emailConfirmationTokenDAO.sendEmail(
         user,
         tokenKey,
         UserConstants.SUBJECTFORREGISTRATION,
         UserConstants.TEXTFORREGISTRATIONEMAIL,
         ShortLinkConstants.EMAIL_VERIFICATION_URL
      );
      return null;
   }

   private Void sendEmailForPasswordVerification(User user, String tokenKey) {
      emailConfirmationTokenDAO.sendEmail(
         user,
         tokenKey,
         UserConstants.SUBJECTFORFORGOTEMAILVERIFY,
         UserConstants.TEXTFORFORGOTPASSWORDEMAIL,
         ShortLinkConstants.PASSWORD_VERIFICATION_URL
      );
      return null;
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
                        saveKeyIndicesAndSendEmail(token.getKeyGeneratorResponse().getHashKey(), token.getNextIndices(), user, true));
               })
         );
   }

   public Mono<SignUpResponse> verifyTokenAndSaveUser(String token) {
      return emailTokenService.findByToken(token)
         .flatMap(confirmedToken -> {
            Duration tokenAge = Duration.between(confirmedToken.getUpdateTime(), ZonedDateTime.now());

            if (tokenAge.toMinutes() > EMAIL_VERIFICATION_TOKEN_AGE) {
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

   public Mono<String> resetPassword(ResetPasswordRequest req) {
      return userDetailsService.getLoggedInUser()
         .flatMap(user -> {
            if (passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
               return userRepository.resetPasswordByEmail(user.getUsername(), passwordEncoder.encode(req.getNewPassword()))
                  .flatMap(updatePassword -> Mono.just(UserConstants.PASSWORD_RESET_SUCCESSFULLY))
                  .switchIfEmpty(Mono.error(new InternalServerError(COMMON_INTERNAL_SERVER_MESSAGE)));
            } else {
               return Mono.error(new BadRequestException(UserConstants.CURRENT_PASSWORD_INCORRECT_MESSAGE));
            }
         });
   }

   public Mono<String> resetForgotPassword(ForgetPasswordRequest req) {
      return emailTokenService.findByToken(req.getToken())
         .flatMap(token -> userRepository
            .resetPasswordByEmail(token.getUserEmail(), passwordEncoder.encode(req.getPassword()))
            .flatMap(updatePassword -> Mono.just(UserConstants.PASSWORD_RESET_SUCCESSFULLY))
            .switchIfEmpty(Mono.error(new InternalServerError(COMMON_INTERNAL_SERVER_MESSAGE)))
         )
         .switchIfEmpty(Mono.error(new BadRequestException(UserConstants.TOKEN_DOES_NOT_EXIST)));
   }

   public Mono<String> sendVerificationLinkForForgotPassword(VerifyEmailForgetPasswordRequest req) {
      return userRepository.findUserByEmail(req.getEmail())
         .flatMap(user -> emailConfirmationTokenDAO.getConfirmationToken()
            .flatMap(token -> {
               EmailConfirmationToken confirmationToken = new EmailConfirmationToken(
                  token.getKeyGeneratorResponse().getHashKey(), user.getEmail()
               );
               return emailTokenService.save(confirmationToken)
                  .flatMap(savedOrUpdatedToken ->
                     saveKeyIndicesAndSendEmail(
                        token.getKeyGeneratorResponse().getHashKey(),
                        token.getNextIndices(),
                        user,
                        false
                     )
                  );
            })
         )
         .switchIfEmpty(Mono.error(new BadRequestException(
            String.format(UserConstants.USER_DOES_NOT_EXIST_MESSAGE, req.getEmail())
         )));
   }
}

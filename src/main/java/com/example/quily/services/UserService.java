package com.example.quily.services;

import com.example.quily.constants.UserConstants;
import com.example.quily.dao.EmailConfirmationTokenDAO;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.model.User;
import com.example.quily.repositories.UserRepository;
import com.example.quily.response.SignUpResponse;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
   private final UserRepository userRepository;
   private final EmailTokenService emailTokenService;
   private final EmailConfirmationTokenDAO emailConfirmationTokenDAO;
   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UserService(UserRepository userRepository, EmailTokenService emailTokenService, EmailConfirmationTokenDAO emailConfirmationTokenDAO, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.emailTokenService = emailTokenService;
      this.emailConfirmationTokenDAO = emailConfirmationTokenDAO;
      this.passwordEncoder = passwordEncoder;
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
      final String token = emailConfirmationTokenDAO.getConfirmationToken(user);
      final EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, user.getEmail(), CommonUtil.getCurrentDateTimeInFormat());
      final String message = String.format(UserConstants.EMAIL_VERIFICATION_MESSAGE, user.getEmail());
      Mono<EmailConfirmationToken> saveToken = emailTokenService.save(confirmationToken);
      Mono<Void> sentEmail = Mono.fromRunnable(() -> emailConfirmationTokenDAO.sendEmail(user, token));
      Mono<User> savedUser = createUser(user);

      return Mono.when(saveToken, sentEmail, savedUser)
         .then(Mono.fromCallable(() -> message));
   }

   public Mono<SignUpResponse> verifyTokenAndSaveUser(String token) {
      return emailTokenService.findByToken(token)
         .flatMap(confirmedToken -> {
            final Mono<Long> activatedUser = userRepository.makeUserActive(confirmedToken.getUserEmail());
            final Mono<Long> deletedToken = emailTokenService.deleteByToken(token);
            return Mono.zip(activatedUser, deletedToken)
               .map(result ->
                  new SignUpResponse(confirmedToken.getUserEmail(), CommonUtil.getCurrentDateTimeInFormat())
               );
         });
   }

   public Mono<User> getUserByEmail(String email) {
      return userRepository.findUserByEmail(email);
   }

   public Mono<User> getUserAndCheckCredentials(String email, String password) {
      return getUserByEmail(email)
         .filter(user -> passwordEncoder.matches(password, user.getPassword()));
   }
}

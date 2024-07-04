package com.example.quily.security;

import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.example.quily.constants.UserConstants.USER_NOT_FOUND_MESSAGE;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
   private final UserRepository userRepository;

   @Autowired
   public UserDetailsServiceImpl(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   public Mono<UserDetails> findByUsername(String username) {
      return userRepository.findUserByEmail(username)
         .map(UserDetailsImpl::new);
   }
}


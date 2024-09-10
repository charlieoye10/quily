package com.example.quily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

import static com.example.quily.router.ShortLinkRouter.REDIRECT_TO_ORIGINAL_LINK;
import static com.example.quily.router.UserRouter.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

   @Bean
   SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http,
      ReactiveAuthenticationManager authenticationManager,
      ServerAuthenticationConverter authenticationConverter) {

      AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
      authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

      String[] postRoutes = {
         SIGNUP_URL,
         LOGIN_URL,
         SEND_FORGET_PASSWORD_URL
      };

      String[] putRoutes = {
         FORGOT_PASSWORD_URL
      };

      String[] getRoutes = {
         CONFIRM_ACCOUNT_URL,
         REDIRECT_TO_ORIGINAL_LINK
      };

      return http
         .authorizeExchange(exchanges -> exchanges
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers(HttpMethod.POST, postRoutes).permitAll()
            .pathMatchers(HttpMethod.PUT, putRoutes).permitAll()
            .pathMatchers(HttpMethod.GET, getRoutes).permitAll()
            .anyExchange().authenticated()
         )
         .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
         .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
         .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
         .csrf(ServerHttpSecurity.CsrfSpec::disable)
         .cors(withDefaults())
         .build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
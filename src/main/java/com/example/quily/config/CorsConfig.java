package com.example.quily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.example.quily.constants.ShortLinkConstants.FE_LOCALHOST_URL;
import static com.example.quily.constants.ShortLinkConstants.NETLIFY_FE_DOMAIN;

@Configuration
//@EnableWebFlux
public class CorsConfig {

   @Bean
   public CorsWebFilter corsWebFilter() {
      CorsConfiguration corsConfiguration = new CorsConfiguration();
      corsConfiguration.setAllowCredentials(true);
      corsConfiguration.setAllowedOrigins(List.of(FE_LOCALHOST_URL, NETLIFY_FE_DOMAIN));
      corsConfiguration.addAllowedHeader("*");
      corsConfiguration.addAllowedMethod("*");

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", corsConfiguration);

      return new CorsWebFilter(source);
   }
}
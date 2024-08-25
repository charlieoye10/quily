package com.example.quily.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken {
   private final Object principal;

   public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal) {
      super(authorities);
      this.principal = principal;
   }

   public void setUserAuthenticated() {
      super.setAuthenticated(true);
   }

   @Override
   public Object getCredentials() {
      return null;
   }

   @Override
   public Object getPrincipal() {
      return principal;
   }
}

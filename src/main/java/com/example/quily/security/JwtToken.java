package com.example.quily.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtToken extends AbstractAuthenticationToken {
   public JwtToken(Collection<? extends GrantedAuthority> authorities) {
      super(authorities);
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
      return null;
   }
}

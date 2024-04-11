package com.sideproject.config.security;


import com.sideproject.backoffice.admin.AdminService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

  private AdminService adminService;

  public CustomDaoAuthenticationProvider(
      AdminService adminService,
      BCryptPasswordEncoder bCryptPasswordEncoder
  ) {
    this.adminService = adminService;
    setUserDetailsService(adminService);
    setPasswordEncoder(bCryptPasswordEncoder);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    try {
      Authentication auth = super.authenticate(authentication);

      if (auth.getPrincipal() instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        adminService.resetFailCount(userDetails.getUsername());
      }

      return auth;
    } catch (BadCredentialsException e) {
      if (authentication.getPrincipal() instanceof String) {
        String username = (String) authentication.getPrincipal();
        adminService.increaseFailCount(username);
      }
      throw e;
    } catch (AuthenticationException e) {
      throw e;
    }
  }
}
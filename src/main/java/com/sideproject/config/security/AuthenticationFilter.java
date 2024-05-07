package com.sideproject.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.backoffice.admin.AdminService;
import com.sideproject.common.APIDataResponse;
import com.sideproject.common.APIErrorResponse;
import com.sideproject.common.CProperties;
import com.sideproject.config.jwt.JwtTokenProvider;
import com.sideproject.config.jwt.Token;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import com.sideproject.domain.dto.login.LoginRequestDto;
import com.sideproject.domain.dto.login.LoginResponseDto;
import com.sideproject.domain.entity.AuthEntity;
import com.sideproject.domain.repository.AuthRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sideproject.domain.enums.ErrorCode.LOCKED_ACCOUNT;
import static com.sideproject.domain.enums.ErrorCode.LOGIN_FAIL;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter implements AuthenticationFailureHandler {

  /* todo
      RSA 암호화 session을 사용하지 않으므로 DB 이용하여 진행
  */

  private AdminService adminService;
  private CProperties cProperties;

  private JwtTokenProvider jwtTokenProvider;

  public AuthenticationFilter(AuthenticationManager authenticationManager,
                              AdminService adminService,
                              CProperties cProperties,
                              JwtTokenProvider jwtTokenProvider) {
    super.setAuthenticationManager(authenticationManager);
    super.setAuthenticationFailureHandler(this::onAuthenticationFailure);
    this.adminService = adminService;
    this.cProperties = cProperties;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws AuthenticationException {
    try{
      LoginRequestDto creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              creds.getEmail(),
              creds.getPassword(),
              new ArrayList<>()
          )
      );
    }catch(IOException e){
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    String userName = ((User)authResult.getPrincipal()).getUsername();
    AdminResponseDto admin = adminService.getAdminDetailsByEmail(userName);

    Map<String, Object> claims = new HashMap<>();
    claims.put("adminId",admin.getAdminId());
    claims.put("email",admin.getEmail());
    claims.put("name",admin.getName());
    claims.put("status",admin.getStatus());
    claims.put("menuId",admin.getMenuId());
    claims.put("funcId",admin.getFuncId());

    Token jwtToken = jwtTokenProvider.generateTokenHS512(
        admin.getEmail(),
        cProperties.getJwt().getAccessTimeoutMin(),
        cProperties.getJwt().getSecret(),
        claims);

    jwtTokenProvider.createAccessCookie(
        response,
        jwtToken.getAccessToken(),
        cProperties.getJwt().getAccessHeader(),
        cProperties.getJwt().getAccessTimeoutMin());

    jwtTokenProvider.createRefreshCookie(
        response,
        jwtToken.getRefreshToken(),
        cProperties.getJwt().getRefreshHeader(),
        cProperties.getJwt().getRefreshTimeoutMin());

    LoginResponseDto loginResponseDto = LoginResponseDto.builder()
        .adminId(admin.getAdminId())
        .email(admin.getEmail())
        .name(admin.getName())
        .status(admin.getStatus())
        .menuId(admin.getMenuId())
        .funcId(admin.getFuncId())
        .build();

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    try(PrintWriter writer = response.getWriter()){
      writer.write(new ObjectMapper().writeValueAsString(APIDataResponse.of(loginResponseDto)));
    }catch (IOException e){
      e.printStackTrace();
    }
  }



  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    jwtTokenProvider.resetAccessCookie(response, cProperties.getJwt().getAccessHeader());
    jwtTokenProvider.resetRefreshCookie(response,  cProperties.getJwt().getRefreshHeader());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    try (PrintWriter writer = response.getWriter()) {
      int errorCode = LOGIN_FAIL.getCode();
      String errorMessage = LOGIN_FAIL.getMessage();

      if(exception.getMessage().equals(LOCKED_ACCOUNT.getMessage())){
        errorCode = LOCKED_ACCOUNT.getCode();
        errorMessage = LOCKED_ACCOUNT.getMessage();
      }
      writer.write(new ObjectMapper().writeValueAsString(APIErrorResponse.of(false, errorCode, errorMessage)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

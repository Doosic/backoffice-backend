package com.sideproject.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.backoffice.admin.AdminService;
import com.sideproject.common.APIErrorResponse;
import com.sideproject.common.CProperties;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import com.sideproject.domain.entity.AuthEntity;
import com.sideproject.domain.repository.AuthRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.sideproject.domain.enums.ErrorCode.UNAUTHORIZED_FAIL;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private final AdminService adminService;
  private final CProperties cProperties;
  private final JwtTokenProvider jwtTokenProvider;

  private AntPathMatcher pathMatcher = new AntPathMatcher();

  // 인증에서 제외할 url
  private Set<String> skipUrls = new HashSet<>(Arrays.asList(
      "/login",
      "/logout",
      "/login/**",
      "/ep/**"
  ));

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return skipUrls.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    if(request.getCookies() == null){
      this.unathorizedFail(response);
    }

    String accessToken = Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals(cProperties.getJwt().getAccessHeader()))
        .findFirst().map(Cookie::getValue)
        .orElse(null);

    String refreshToken = Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals(cProperties.getJwt().getRefreshHeader()))
        .findFirst().map(Cookie::getValue)
        .orElse(null);

    String userEmail = null;

    // refreshToken을 이용한 재발급
    if(accessToken != null && refreshToken != null && jwtTokenProvider.validateToken(accessToken)){
      if(jwtTokenProvider.validateToken(accessToken)){
        userEmail = jwtTokenProvider.getUsernameFromToken(accessToken);
      }else if(jwtTokenProvider.validateToken(refreshToken)){
        userEmail = jwtTokenProvider.getUsernameFromToken(refreshToken);

        AdminResponseDto admin = adminService.getAdminDetailsByEmail(userEmail);

        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId",admin.getAdminId());
        claims.put("email",admin.getEmail());
        claims.put("name",admin.getName());
        claims.put("status",admin.getStatus());
        claims.put("authId",admin.getAuthId());

        Token jwtToken = jwtTokenProvider.generateTokenHS512(
            userEmail,
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
            cProperties.getJwt().getAccessTimeoutMin());
      }
    }

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
      Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }


  private void unathorizedFail(HttpServletResponse response){
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    try (PrintWriter writer = response.getWriter()) {
      writer.write(new ObjectMapper().writeValueAsString(APIErrorResponse.of(false, UNAUTHORIZED_FAIL.getCode(), UNAUTHORIZED_FAIL.getMessage())));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

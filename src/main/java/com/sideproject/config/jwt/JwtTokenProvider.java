package com.sideproject.config.jwt;

import com.sideproject.common.CProperties;
import com.sideproject.domain.dto.admin.AdminInfo;
import com.sideproject.domain.enums.AdminStatusCode;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final CProperties cProperties;

  public Token generateTokenHS512(
      String adminId,
      Integer timeOutMin,
      String secret,
      Map<String, Object> claims
  ){
    String accessToken = Jwts.builder()
        .setClaims(claims)
        .setId(adminId)
        .setExpiration(new Date(System.currentTimeMillis() + this.getTokenExpirationTime(timeOutMin)))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();

    String refreshToken = Jwts.builder()
        .setId(adminId)
        .setExpiration(new Date(System.currentTimeMillis() + this.getTokenExpirationTime(timeOutMin)))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();

    return Token.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  private Integer getTokenExpirationTime(Integer expirationTime) {
    return 1000 * 60 * expirationTime;
  }

  public void createAccessCookie(
      HttpServletResponse response,
      String accessToken,
      String accessHeader,
      Integer timeOutMin
  ) {
    Cookie cookieAccess = new Cookie(accessHeader, accessToken);
    cookieAccess.setPath("/");
    cookieAccess.setMaxAge(60 * 60 * timeOutMin);
    cookieAccess.setHttpOnly(true);
    response.addCookie(cookieAccess);
  }

  public void createRefreshCookie(
      HttpServletResponse response,
      String refreshToken,
      String refreshHeader,
      Integer timeOutMin
  ) {
    Cookie cookieRefresh = new Cookie(refreshHeader, refreshToken);
    cookieRefresh.setPath("/");
    cookieRefresh.setMaxAge(60 * 60 * timeOutMin);
    cookieRefresh.setHttpOnly(true);
    response.addCookie(cookieRefresh);
  }

  public void resetAccessCookie(
      HttpServletResponse response,
      String accessHeader
  ){
    Cookie cookieAccess = new Cookie(accessHeader, null);
    cookieAccess.setPath("/");
    cookieAccess.setMaxAge(0);
    cookieAccess.setHttpOnly(true);
    response.addCookie(cookieAccess);
  }

  public void resetRefreshCookie(
      HttpServletResponse response,
      String refreshHeader
  ){
    Cookie cookieRefresh = new Cookie(refreshHeader, null);
    cookieRefresh.setPath("/");
    cookieRefresh.setMaxAge(0);
    cookieRefresh.setHttpOnly(true);
    response.addCookie(cookieRefresh);
  }

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getId);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(cProperties.getJwt().getSecret()).parseClaimsJws(token).getBody();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(cProperties.getJwt().getSecret()).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  public Authentication getAuthentication(String accessToken) {
    Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
    roles.add(new SimpleGrantedAuthority("ROLE_USER"));

    AdminInfo adminInfo = parseTokenBody(accessToken);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(adminInfo.getEmail(), adminInfo.getEmail(), roles);
    authenticationToken.setDetails(adminInfo);
    return authenticationToken;
  }

  public AdminInfo parseTokenBody(String token){
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(cProperties.getJwt().getSecret())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return new AdminInfo().builder()
        .adminId(Long.parseLong(claims.get("adminId").toString()))
        .email((String) claims.get("email"))
        .name((String) claims.get("name"))
        .status(AdminStatusCode.valueOf((String) claims.get("status")))
        .build();
  }
}

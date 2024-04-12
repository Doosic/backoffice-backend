package com.sideproject.config.security;

import com.sideproject.backoffice.admin.AdminService;
import com.sideproject.common.CProperties;
import com.sideproject.config.jwt.JwtAuthenticationEntryPoint;
import com.sideproject.config.jwt.JwtRequestFilter;
import com.sideproject.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AdminService adminService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ObjectPostProcessor<Object> objectPostProcessor;
  private final CProperties cProperties;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtRequestFilter jwtRequestFilter;

  private static final String[] WHITE_LIST = {
      "/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic(withDefaults())
        .cors().configurationSource(corsConfigurationSource())
        .and()
        .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
        .authorizeHttpRequests((authorize) -> {
              try {
                authorize
                    .requestMatchers(WHITE_LIST).permitAll();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }
        )
        .logout().disable()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).disable()
        .formLogin().disable()
        .addFilter(getAuthenticationFilter())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:4173"));
    config.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT"));
    config.setAllowedHeaders(Arrays.asList("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  private AuthenticationFilter getAuthenticationFilter() throws Exception {
    AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(
        this.authenticationManager(builder),
        adminService,
        cProperties,
        jwtTokenProvider
    );
    return authenticationFilter;
  }


  public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(this.customDaoAuthenticationProvider());
    return auth.build();
  }

  @Bean
  public CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
    CustomDaoAuthenticationProvider authenticationProvider = new CustomDaoAuthenticationProvider(adminService, bCryptPasswordEncoder);
    return authenticationProvider;
  }

}

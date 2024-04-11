package com.sideproject.config.jwt;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Token {

  private String accessToken;
  private String refreshToken;
}

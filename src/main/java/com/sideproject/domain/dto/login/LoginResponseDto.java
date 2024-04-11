package com.sideproject.domain.dto.login;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {

  private Long adminId;
  private String email;
}

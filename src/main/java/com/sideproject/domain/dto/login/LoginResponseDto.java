package com.sideproject.domain.dto.login;

import com.sideproject.domain.enums.AdminStatusCode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {

  private Long adminId;
  private String email;
  private String name;
  private AdminStatusCode status;
  private Long menuId;
  private Long funcId;
}

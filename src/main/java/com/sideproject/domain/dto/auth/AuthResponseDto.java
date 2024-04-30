package com.sideproject.domain.dto.auth;

import com.sideproject.domain.dto.admin.AdminResponseDto;
import com.sideproject.domain.dto.admin.AdminSimpleResponseDto;
import com.sideproject.domain.enums.AuthType;
import lombok.Data;

@Data
public class AuthResponseDto {

  private Long authId;
  private String authName;
  private AuthType authType;
  private AdminSimpleResponseDto regUser;
  private String createDate;
}

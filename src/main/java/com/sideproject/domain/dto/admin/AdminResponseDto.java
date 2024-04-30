package com.sideproject.domain.dto.admin;

import com.sideproject.domain.enums.AdminStatusCode;
import lombok.Data;

@Data
public class AdminResponseDto {

  private Long adminId;
  private Long authId;
  private String email;
  private String name;
  private AdminStatusCode status;
  private String createDate;
}

package com.sideproject.domain.dto.admin;

import com.sideproject.domain.enums.AdminStatusCode;
import lombok.Data;

@Data
public class AdminResponseDto {

  private Long adminId;
  private String email;
  private String name;
  private AdminStatusCode status;
}

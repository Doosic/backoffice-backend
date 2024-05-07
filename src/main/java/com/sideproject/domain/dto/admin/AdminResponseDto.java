package com.sideproject.domain.dto.admin;

import com.sideproject.domain.enums.AdminStatusCode;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class AdminResponseDto {

  private Long adminId;
  private Long menuId;
  private Long funcId;
  private String email;
  private String name;
  private AdminStatusCode status;
  private String createDate;
}

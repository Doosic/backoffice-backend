package com.sideproject.domain.dto.admin;

import com.sideproject.domain.enums.AdminStatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfo {

  private Long adminId;
  private String email;
  private String name;
  private AdminStatusCode status;
  private Long authId;
}

package com.sideproject.domain.dto.role;

import com.sideproject.domain.dto.admin.AdminSimpleResponseDto;
import com.sideproject.domain.enums.RoleType;
import lombok.Data;

@Data
public class RoleResponseDto {

  private Long roleId;
  private String authName;
  private RoleType roleType;
  private AdminSimpleResponseDto regUser;
  private String createDate;
}

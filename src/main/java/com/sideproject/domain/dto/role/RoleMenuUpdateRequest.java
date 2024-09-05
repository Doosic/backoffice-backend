package com.sideproject.domain.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleMenuUpdateRequest {

  @NotNull(message = "RoleId cannot be null")
  private Long roleId;

  @NotNull(message = "Menu keys cannot be null")
  private Long[] menuKeys;

}
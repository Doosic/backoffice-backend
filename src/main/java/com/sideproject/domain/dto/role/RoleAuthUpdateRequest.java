package com.sideproject.domain.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleAuthUpdateRequest {

  @NotNull(message = "RoleId cannot be null")
  private Long roleId;

  @NotNull(message = "Auth keys cannot be null")
  private Long[] authKeys;

}
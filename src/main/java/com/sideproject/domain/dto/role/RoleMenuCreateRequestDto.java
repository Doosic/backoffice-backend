package com.sideproject.domain.dto.role;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleMenuCreateRequestDto {

  @NotNull(message = "Auth name cannot be null")
  @Size(min = 2, message = "Email not be less than two characters")
  private String authName;

  @NotNull(message = "Menu keys cannot be null")
  private Long[] menuKeys;
}

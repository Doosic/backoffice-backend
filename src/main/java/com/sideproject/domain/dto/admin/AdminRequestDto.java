package com.sideproject.domain.dto.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRequestDto {

  @NotNull(message = "AdminId cannot be null")
  private Long adminId;

  @NotNull(message = "Email cannot be null")
  @Size(min = 2, message = "Email not be less than two characters")
  private String email;

}

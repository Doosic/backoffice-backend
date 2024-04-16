package com.sideproject.domain.dto.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminCreateRequestDto {

  @NotNull(message = "Email cannot be null")
  @Size(min = 6, message = "Email not be less than two characters")
  private String email;

  @NotNull(message = "Name cannot be null")
  @Size(min = 3, message = "Name not be less than two characters")
  private String name;

  @NotNull(message = "Password cannot be null")
  @Size(min = 8, message = "Password must be equal or grater than 8 characters")
  private String password;
}

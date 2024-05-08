package com.sideproject.domain.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthFuncUpdateRequest {

  @NotNull(message = "AuthId cannot be null")
  private Long authId;

  @NotNull(message = "Function keys cannot be null")
  private Long[] funcKeys;

}
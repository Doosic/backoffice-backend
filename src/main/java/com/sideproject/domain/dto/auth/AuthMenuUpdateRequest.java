package com.sideproject.domain.dto.auth;

import com.sideproject.domain.entity.AdminEntity;
import com.sideproject.domain.entity.AuthEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthMenuUpdateRequest {

  @NotNull(message = "AuthId cannot be null")
  private Long authId;

  @NotNull(message = "Menu keys cannot be null")
  private Long[] menuKeys;

}
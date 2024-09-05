package com.sideproject.domain.dto.auth;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponseDto {

  private Long key;
  private Long funcParent;
  private String label;
  private String icon;
  private List<AuthResponseDto> children;

}

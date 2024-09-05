package com.sideproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

  MENU("MENU"),
  AUTH("AUTH");

  private final String status;
}

package com.sideproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthType {

  MENU("MENU"),
  FUNC("FUNC");

  private final String status;
}

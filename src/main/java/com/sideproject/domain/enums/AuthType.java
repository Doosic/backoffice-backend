package com.sideproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthType {

  ITEM("MENU"),
  TREE("FUNC");

  private final String status;
}

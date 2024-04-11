package com.sideproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminStatusCode {

  DELETE("DELETE"),
  LOCK("LOCK"),
  USE("USE");

  private final String status;
}

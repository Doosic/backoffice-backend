package com.sideproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuTypeCode {

  ITEM("ITEM"),
  TREE("TREE");

  private final String status;
}

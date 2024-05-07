package com.sideproject.domain.dto.function;

import lombok.Data;

import java.util.List;

@Data
public class FunctionResponseDto {

  private Long key;
  private Long funcParent;
  private String label;
  private String icon;
  private List<FunctionResponseDto> children;

}

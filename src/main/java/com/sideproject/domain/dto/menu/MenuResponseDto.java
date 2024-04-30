package com.sideproject.domain.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class MenuResponseDto {
  private Long key;
  private Long menuParent;
  private String label;
  private String icon;
  private String to;
  private String query;
  private List<MenuResponseDto> children;
}
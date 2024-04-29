package com.sideproject.domain.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDto {

  private Long menuId;
  private Long menuParent;
  private String label;
  private String icon;
  private String to;
  private List<MenuResponseDto> items;
}
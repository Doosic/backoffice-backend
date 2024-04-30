package com.sideproject.domain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.sideproject.common.BaseTimeEntity;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import com.sideproject.domain.enums.MenuTypeCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "bk_menu")
public class MenuEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long menuId;

  @Column(name = "menu_parent")
  private Long menuParent;

  @Column(name = "menu_name")
  private String menuName;

  @Column(name = "menu_level")
  private Integer menuLevel;

  @Column(name = "menu_order")
  private Integer menuOrder;

  @Column(name = "menu_type")
  @Enumerated(EnumType.STRING)
  private MenuTypeCode menuTypeCode;

  @Column(name = "menu_icon")
  private String menuIcon;

  @Column(name = "menu_link")
  private String menuLink;

  @Column(name = "menu_query")
  private String menuQuery;

  public MenuEntity () {};

  public MenuResponseDto toDto(){
    MenuResponseDto menu = new MenuResponseDto();
    menu.setKey(this.getMenuId());
    menu.setMenuParent(this.getMenuParent());
    menu.setLabel(this.getMenuName());
    menu.setIcon(this.getMenuIcon());
    menu.setTo(this.getMenuLink());
    menu.setQuery(this.getMenuQuery());
    return menu;
  }

}

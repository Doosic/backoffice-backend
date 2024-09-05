package com.sideproject.domain.entity;

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
@Table(name = "bk_role_menu")
public class RoleMenuEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_role_id")
  private Long menuRoleId;

  @Column(name = "role_id")
  private Long roleId;

  @Column(name = "menu_id")
  private Long menuId;

  public RoleMenuEntity() {};
}

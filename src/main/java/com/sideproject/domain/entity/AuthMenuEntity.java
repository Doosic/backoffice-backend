package com.sideproject.domain.entity;

import com.sideproject.domain.enums.AuthType;
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
@Table(name = "bk_auth_menu")
public class AuthMenuEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "auth_menu_id")
  private Long authMenuId;

  @Column(name = "auth_id")
  private Long authId;

  @Column(name = "menu_id")
  private Long menuId;

  public AuthMenuEntity() {};
}

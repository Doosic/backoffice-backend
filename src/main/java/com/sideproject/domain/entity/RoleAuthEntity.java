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
@Table(name = "bk_role_auth")
public class RoleAuthEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "auth_role_id")
  private Long funcRoleId;

  @Column(name = "role_id")
  private Long roleId;

  @Column(name = "auth_id")
  private Long authId;

  @Column(name = "auth_name")
  private String authName;

  public RoleAuthEntity() {};
}

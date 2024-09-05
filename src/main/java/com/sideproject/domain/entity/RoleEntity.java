package com.sideproject.domain.entity;

import com.sideproject.common.BaseTimeEntity;
import com.sideproject.domain.dto.role.RoleResponseDto;
import com.sideproject.domain.enums.RoleType;
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
@Table(name = "bk_role")
public class RoleEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long roleId;

  @Column(name = "reg_user")
  private Long regUser;

  @Column(name = "auth_name")
  private String authName;

  @Column(name = "auth_type")
  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  public RoleEntity() {};

  public RoleResponseDto toDto(){
    RoleResponseDto auth = new RoleResponseDto();
    auth.setAuthName(this.getAuthName());
    auth.setRoleType(this.getRoleType());
    auth.setRoleId(this.getRoleId());
    return auth;
  }
}

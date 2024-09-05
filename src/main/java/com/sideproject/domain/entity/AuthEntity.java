package com.sideproject.domain.entity;

import com.sideproject.domain.dto.auth.AuthResponseDto;
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
@Table(name = "bk_auth")
public class AuthEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "auth_id")
  private Long authId;

  @Column(name = "auth_parent")
  private Long authParent;

  @Column(name = "auth_name")
  private String authName;

  @Column(name = "auth_level")
  private Integer authLevel;

  @Column(name = "auth_order")
  private Integer authOrder;

  @Column(name = "menu_type")
  @Enumerated(EnumType.STRING)
  private MenuTypeCode menuTypeCode;

  @Column(name = "auth_icon")
  private String authIcon;

  public AuthEntity() {};

  public AuthResponseDto toDto() {
    AuthResponseDto func = new AuthResponseDto();
    func.setKey(this.getAuthId());
    func.setFuncParent(this.getAuthParent());
    func.setLabel(this.getAuthName());
    func.setIcon(this.getAuthIcon());
    return func;
  }
}

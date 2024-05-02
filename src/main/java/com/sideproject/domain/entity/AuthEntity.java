package com.sideproject.domain.entity;

import com.sideproject.common.BaseTimeEntity;
import com.sideproject.domain.dto.auth.AuthResponseDto;
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
@Table(name = "bk_auth")
public class AuthEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "auth_id")
  private Long authId;

  @Column(name = "reg_user")
  private Long regUser;

  @Column(name = "auth_name")
  private String authName;

  @Column(name = "auth_type")
  @Enumerated(EnumType.STRING)
  private AuthType authType;

  public AuthEntity() {};

  public AuthResponseDto toDto(){
    AuthResponseDto auth = new AuthResponseDto();
    auth.setAuthName(this.getAuthName());
    auth.setAuthType(this.getAuthType());
    auth.setAuthId(this.getAuthId());
    return auth;
  }
}

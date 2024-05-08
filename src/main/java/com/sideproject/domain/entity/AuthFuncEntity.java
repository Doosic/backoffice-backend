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
@Table(name = "bk_auth_function")
public class AuthFuncEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "auth_func_id")
  private Long authFuncId;

  @Column(name = "auth_id")
  private Long authId;

  @Column(name = "func_id")
  private Long funcId;

  @Column(name = "func_name")
  private String funcName;

  public AuthFuncEntity () {};
}

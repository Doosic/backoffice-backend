package com.sideproject.domain.entity;

import com.sideproject.domain.dto.function.FunctionResponseDto;
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
@Table(name = "bk_function")
public class FunctionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "func_id")
  private Long funcId;

  @Column(name = "func_parent")
  private Long funcParent;

  @Column(name = "func_name")
  private String funcName;

  @Column(name = "func_level")
  private Integer funcLevel;

  @Column(name = "func_order")
  private Integer funcOrder;

  @Column(name = "menu_type")
  @Enumerated(EnumType.STRING)
  private MenuTypeCode menuTypeCode;

  @Column(name = "func_icon")
  private String funcIcon;

  public FunctionEntity () {};

  public FunctionResponseDto toDto() {
    FunctionResponseDto func = new FunctionResponseDto();
    func.setKey(this.getFuncId());
    func.setFuncParent(this.getFuncParent());
    func.setLabel(this.getFuncName());
    func.setIcon(this.getFuncIcon());
    return func;
  }
}

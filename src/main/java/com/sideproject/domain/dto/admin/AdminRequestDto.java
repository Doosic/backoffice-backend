package com.sideproject.domain.dto.admin;

import com.sideproject.domain.dto.pagenation.Pagenation;
import com.sideproject.domain.enums.AdminStatusCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRequestDto extends Pagenation {

  private Long adminId;
  private String email;
  private String name;
  private AdminStatusCode status;

}

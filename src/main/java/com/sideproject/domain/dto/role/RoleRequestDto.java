package com.sideproject.domain.dto.role;

import com.sideproject.domain.dto.pagenation.Pagenation;
import com.sideproject.domain.enums.RoleType;
import lombok.Data;

@Data
public class RoleRequestDto extends Pagenation {

  private String searchTitle;
  private String searchText;
  private RoleType status;
}

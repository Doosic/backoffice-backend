package com.sideproject.domain.dto.auth;

import com.sideproject.domain.dto.pagenation.Pagenation;
import com.sideproject.domain.enums.AuthType;
import lombok.Data;

@Data
public class AuthRequestDto extends Pagenation {

  private String searchTitle;
  private String searchText;
  private AuthType status;
}

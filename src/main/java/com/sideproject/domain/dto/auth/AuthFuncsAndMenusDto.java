package com.sideproject.domain.dto.auth;

import com.sideproject.domain.dto.function.FunctionResponseDto;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class AuthFuncsAndMenusDto {

  List<MenuResponseDto> menuList;
  List<FunctionResponseDto> funcList;
}

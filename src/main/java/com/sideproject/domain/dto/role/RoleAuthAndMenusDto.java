package com.sideproject.domain.dto.role;

import com.sideproject.domain.dto.auth.AuthResponseDto;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class RoleAuthAndMenusDto {

  List<MenuResponseDto> menuList;
  List<AuthResponseDto> authList;
}

package com.sideproject.backoffice.role;

import com.sideproject.annotation.BeforeRequiresAuthorization;
import com.sideproject.backoffice.auth.AuthService;
import com.sideproject.backoffice.menu.MenuService;
import com.sideproject.common.APIDataResponse;
import com.sideproject.common.APIResponseList;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.role.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RoleController extends BaseController {

  private final RoleService roleService;
  private final MenuService menuService;
  private final AuthService authService;

  @GetMapping("/bs/auths")
  public ResponseEntity<byte[]> getAuths(
      RoleRequestDto roleRequestDto
  ) {
    Page<RoleResponseDto> auths = roleService.getAuths(roleRequestDto);

    APIResponseList APIResponseList = new APIResponseList();
    APIResponseList.setItemList(auths.getContent());
    APIResponseList.setItemTotalCnt(auths.getTotalElements());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(APIResponseList));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @PostMapping("/bs/auth-menu")
  @BeforeRequiresAuthorization("auth-menu-create")
  public APIDataResponse<RoleResponseDto> createAuthAndMenu(
      @Validated @RequestBody RoleMenuCreateRequestDto roleMenuCreateRequestDto
      ) {
    RoleResponseDto roleResponseDto = roleService.createAuthAndMenu(this.getSessionInfo().getAdminId(), roleMenuCreateRequestDto);

    return APIDataResponse.of(roleResponseDto);
  }

  @PostMapping("/bs/auth-func")
  @BeforeRequiresAuthorization("auth-func-create")
  public APIDataResponse<RoleResponseDto> createAuthAndFunc(
      @Validated @RequestBody RoleAuthCreateRequestDto authMenuCreateRequestDto
  ) {
    RoleResponseDto roleResponseDto = roleService.createAuthAndFunc(this.getSessionInfo().getAdminId(), authMenuCreateRequestDto);

    return APIDataResponse.of(roleResponseDto);
  }

  @PutMapping("/bs/auth-menu")
  @BeforeRequiresAuthorization("auth-menu-update")
  public APIDataResponse<RoleResponseDto> updateAuthAndMenu(
      @Validated @RequestBody RoleMenuUpdateRequest roleMenuUpdateRequest
  ) {
    RoleResponseDto roleResponseDto = roleService.updateAuthAndMenu(this.getSessionInfo().getAdminId(), roleMenuUpdateRequest);

    return APIDataResponse.of(roleResponseDto);
  }

  @PutMapping("/bs/auth-func")
  @BeforeRequiresAuthorization("auth-func-update")
  public APIDataResponse<RoleResponseDto> updateAuthAndFunc(
      @Validated @RequestBody RoleAuthUpdateRequest roleAuthUpdateRequest
  ) {
    RoleResponseDto roleResponseDto = roleService.updateAuthAndFunc(this.getSessionInfo().getAdminId(), roleAuthUpdateRequest);

    return APIDataResponse.of(roleResponseDto);
  }

  @GetMapping("/bs/all-auth-menu")
  public ResponseEntity<byte[]> getAllFuncAndMenu() {
    RoleAuthAndMenusDto funcsAndMenusDto = new RoleAuthAndMenusDto();
    funcsAndMenusDto.setMenuList(menuService.getMenus());
    funcsAndMenusDto.setAuthList(authService.getAuths());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(funcsAndMenusDto));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }
}
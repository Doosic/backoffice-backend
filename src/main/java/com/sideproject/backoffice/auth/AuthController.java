package com.sideproject.backoffice.auth;

import com.sideproject.backoffice.menu.MenuService;
import com.sideproject.common.APIDataResponse;
import com.sideproject.common.APIResponseList;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.auth.AuthMenuUpdateRequest;
import com.sideproject.domain.dto.auth.AuthRequestDto;
import com.sideproject.domain.dto.auth.AuthResponseDto;
import com.sideproject.domain.dto.auth.AuthMenuCreateRequestDto;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController extends BaseController {

  private final AuthService authService;
  private final MenuService menuService;

  @GetMapping("/bs/auths")
  public ResponseEntity<byte[]> getAuths(
      AuthRequestDto authRequestDto
  ) {
    Page<AuthResponseDto> auths = authService.getAuths(authRequestDto);

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
  public APIDataResponse<AuthResponseDto> createAuthAndMenu(
      @Validated @RequestBody AuthMenuCreateRequestDto authMenuCreateRequestDto
      ) {
    AuthResponseDto authResponseDto = authService.createAuthAndMenu(this.getSessionInfo().getAdminId(), authMenuCreateRequestDto);

    return APIDataResponse.of(authResponseDto);
  }

  @PutMapping("/bs/auth-menu")
  public APIDataResponse<AuthResponseDto> updateAuthAndMenu(
      @Validated @RequestBody AuthMenuUpdateRequest authMenuUpdateRequest
  ) {
    AuthResponseDto authResponseDto = authService.updateAuthAndMenu(this.getSessionInfo().getAdminId(), authMenuUpdateRequest);

    return APIDataResponse.of(authResponseDto);
  }
}

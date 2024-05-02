package com.sideproject.backoffice.menu;

import com.sideproject.common.APIDataResponse;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.menu.MenuRequestDto;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import com.sideproject.domain.dto.menu.MenuSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MenuController extends BaseController {

  private final MenuService menuService;

  @GetMapping("/bs/all-menu")
  public ResponseEntity<byte[]> getAllMenu() {
    List<MenuResponseDto> menuResponseDtos = menuService.getMenus();

    String jsonData = this.convertObjectToJson(APIDataResponse.of(menuResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @GetMapping("/bs/menu-keys")
  public ResponseEntity<byte[]> getMenuKeys(
      MenuRequestDto menuRequestDto
  ) {
    List<MenuSimpleResponseDto> menuResponseDtos = menuService.getMenuKeys(menuRequestDto.getAuthId());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(menuResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @GetMapping("/bs/menus")
  public ResponseEntity<byte[]> getMenus() {
    List<MenuResponseDto> menuResponseDtos = menuService.getAuthMenus(this.getSessionInfo().getAuthId());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(menuResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

}
package com.sideproject.backoffice.auth;

import com.sideproject.common.APIDataResponse;
import com.sideproject.common.APIResponseList;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.auth.AuthRequestDto;
import com.sideproject.domain.dto.auth.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController extends BaseController {

  private final AuthService authService;

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
}

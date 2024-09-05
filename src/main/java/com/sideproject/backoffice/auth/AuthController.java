package com.sideproject.backoffice.auth;

import com.sideproject.common.APIDataResponse;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.auth.AuthRequestDto;
import com.sideproject.domain.dto.auth.AuthResponseDto;
import com.sideproject.domain.dto.auth.AuthSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController extends BaseController {

  private final AuthService authService;

  @GetMapping("/bs/all-auth")
  public ResponseEntity<byte[]> getAllAuth() {
    List<AuthResponseDto> authResponseDtos = authService.getAuths();

    String jsonData = this.convertObjectToJson(APIDataResponse.of(authResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @GetMapping("/bs/auth-keys")
  public ResponseEntity<byte[]> getAuthKeys(
      AuthRequestDto authRequestDto
  ){
    List<AuthSimpleResponseDto> funcResponseDtos = authService.getAuthKeys(authRequestDto.getAuthId());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(funcResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @GetMapping("/bs/funcs")
  public ResponseEntity<byte[]> getFuncs(){
    List<AuthResponseDto> authResponseDtos = authService.getAuthFuncs(this.getSessionInfo().getAuthId());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(authResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }
}
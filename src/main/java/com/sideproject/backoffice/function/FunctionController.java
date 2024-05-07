package com.sideproject.backoffice.function;

import com.sideproject.common.APIDataResponse;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.function.FunctionRequestDto;
import com.sideproject.domain.dto.function.FunctionResponseDto;
import com.sideproject.domain.dto.function.FunctionSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FunctionController extends BaseController {

  private final FunctionService functionService;

  @GetMapping("/bs/all-func")
  public ResponseEntity<byte[]> getAllFunc() {
    List<FunctionResponseDto> functionResponseDtos = functionService.getFunctions();

    String jsonData = this.convertObjectToJson(APIDataResponse.of(functionResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @GetMapping("/bs/func-keys")
  public ResponseEntity<byte[]> getFuncKeys(
      FunctionRequestDto functionRequestDto
  ){
    List<FunctionSimpleResponseDto> funcResponseDtos = functionService.getFuncKeys(functionRequestDto.getFuncId());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(funcResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @GetMapping("/bs/funcs")
  public ResponseEntity<byte[]> getFuncs(){
    List<FunctionResponseDto> functionResponseDtos = functionService.getAuthFuncs(this.getSessionInfo().getFuncId());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(functionResponseDtos));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }
}
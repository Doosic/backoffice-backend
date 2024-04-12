package com.sideproject.backoffice.admin;

import com.sideproject.common.APIDataResponse;
import com.sideproject.common.APIResponseList;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.admin.AdminCreateRequestDto;
import com.sideproject.domain.dto.admin.AdminRequestDto;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminController extends BaseController {

  private final AdminService adminService;

  @GetMapping("/bs/admin/session-info")
  public APIDataResponse<AdminResponseDto> getAdminSessionInfo() {
    AdminResponseDto adminResponseDto = new AdminResponseDto();
    adminResponseDto.setAdminId(this.getSessionInfo().getAdminId());
    adminResponseDto.setName(this.getSessionInfo().getName());
    adminResponseDto.setEmail(this.getSessionInfo().getEmail());
    adminResponseDto.setStatus(this.getSessionInfo().getStatus());

    return APIDataResponse.of(adminResponseDto);
  }

  @PostMapping("/bp/admin")
  public APIDataResponse<AdminResponseDto> createAdmin(
      @Validated @RequestBody AdminCreateRequestDto adminCreateRequestDto
      ){
    AdminResponseDto adminResponseDto = adminService.createAdmin(adminCreateRequestDto);

    return APIDataResponse.of(adminResponseDto);
  }

  @GetMapping("/bs/admins")
  public ResponseEntity<byte[]> getAdmins(
    AdminRequestDto adminRequestDto
  ){
    Page<AdminResponseDto> admins = adminService.getAdmins(adminRequestDto);

    APIResponseList APIResponseList = new APIResponseList();
    APIResponseList.setItemList(admins.getContent());
    APIResponseList.setItemTotalCnt(admins.getTotalElements());

    String jsonData = this.convertObjectToJson(APIDataResponse.of(APIResponseList));
    byte[] compressedData = this.compressData(jsonData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header("Content-Encoding", "gzip")
        .body(compressedData);
  }

  @GetMapping("/bs/logout")
  public APIDataResponse<AdminResponseDto> logout(
      HttpServletResponse response
  ){
    adminService.logout(response);

    return APIDataResponse.of(null);
  }
}

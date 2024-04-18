package com.sideproject.backoffice.admin;

import com.sideproject.common.APIDataResponse;
import com.sideproject.common.APIResponseList;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.admin.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AdminController extends BaseController {

  private final AdminService adminService;

  @GetMapping("/bs/admin/session-info")
  public APIDataResponse<AdminInfo> getAdminSessionInfo() {
    AdminInfo adminInfo = new AdminInfo();
    adminInfo.setAdminId(this.getSessionInfo().getAdminId());
    adminInfo.setName(this.getSessionInfo().getName());
    adminInfo.setEmail(this.getSessionInfo().getEmail());
    adminInfo.setStatus(this.getSessionInfo().getStatus());

    return APIDataResponse.of(adminInfo);
  }

  @GetMapping("/bs/admin")
  public APIDataResponse<AdminResponseDto> getAdmin(
      AdminRequestDto adminRequestDto
  ){
    AdminResponseDto adminResponseDto = adminService.getAdmin(adminRequestDto);

    return APIDataResponse.of(adminResponseDto);
  }

  @PostMapping("/bp/admin")
  public APIDataResponse<AdminResponseDto> createAdmin(
      @Validated @RequestBody AdminCreateRequestDto adminCreateRequestDto
  ) {
    AdminResponseDto adminResponseDto = adminService.createAdmin(adminCreateRequestDto);

    return APIDataResponse.of(adminResponseDto);
  }

  @GetMapping("/bs/admins")
  public ResponseEntity<byte[]> getAdmins(
      AdminRequestDto adminRequestDto
  ) {
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

  @PutMapping("/bs/admin")
  public APIDataResponse<AdminResponseDto> updateAdmin(
      @Validated @RequestBody AdminUpdateRequestDto adminUpdateRequestDto
  ) {
    AdminResponseDto adminResponseDto = adminService.updateAdmin(adminUpdateRequestDto);

    return APIDataResponse.of(adminResponseDto);
  }

  @DeleteMapping("/bs/admin")
  public APIDataResponse<AdminResponseDto> deleteAdmin(
      @Validated @RequestBody AdminRequestDto adminRequestDto
  ) {
    AdminResponseDto adminResponseDto = adminService.deleteAdmin(adminRequestDto);

    return APIDataResponse.of(adminResponseDto);
  }

  @PostMapping("/bs/admin/unlock")
  public APIDataResponse<AdminResponseDto> unlockAdmin(
      @Validated @RequestBody AdminRequestDto adminRequestDto
  ) {
    AdminResponseDto adminResponseDto = adminService.unlockAdmin(adminRequestDto);

    return APIDataResponse.of(adminResponseDto);
  }

  @GetMapping("/bs/logout")
  public APIDataResponse<AdminResponseDto> logout(
      HttpServletResponse response
  ) {
    adminService.logout(response);

    return APIDataResponse.of(null);
  }
}

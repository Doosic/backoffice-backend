package com.sideproject.backoffice.admin;

import com.sideproject.common.APIDataResponse;
import com.sideproject.common.BaseController;
import com.sideproject.domain.dto.admin.AdminCreateRequestDto;
import com.sideproject.domain.dto.admin.AdminRequestDto;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import lombok.RequiredArgsConstructor;
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
    AdminRequestDto adminRequestDto = new AdminRequestDto();
    adminRequestDto.setAdminId(this.getSessionInfo().getAdminId());

    AdminResponseDto adminResponseDto = adminService.getAdmin(adminRequestDto);
    return APIDataResponse.of(adminResponseDto);
  }

  @PostMapping("/bp/admin")
  public APIDataResponse<AdminResponseDto> createAdmin(
      @Validated @RequestBody AdminCreateRequestDto adminCreateRequestDto
      ){
    AdminResponseDto adminResponseDto = adminService.createAdmin(adminCreateRequestDto);

    return APIDataResponse.of(adminResponseDto);
  }
}

package com.sideproject.backoffice.admin;

import com.sideproject.domain.dto.admin.AdminCreateRequestDto;
import com.sideproject.domain.dto.admin.AdminRequestDto;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {

  AdminResponseDto getAdminDetailsByEmail(String adminName);

  AdminResponseDto getAdmin(AdminRequestDto adminRequestDto);

  Page<AdminResponseDto> getAdmins(AdminRequestDto adminRequestDto);

  AdminResponseDto createAdmin(AdminCreateRequestDto adminCreateRequestDto);

  void logout(HttpServletResponse response);
  void increaseFailCount(String userEmail);
  void resetFailCount(String userEmail);
}

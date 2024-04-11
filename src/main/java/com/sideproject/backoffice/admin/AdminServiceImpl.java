package com.sideproject.backoffice.admin;

import com.sideproject.common.CProperties;
import com.sideproject.config.jwt.JwtTokenProvider;
import com.sideproject.domain.dto.admin.AdminCreateRequestDto;
import com.sideproject.domain.dto.admin.AdminRequestDto;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import com.sideproject.domain.entity.AdminEntity;
import com.sideproject.domain.enums.AdminStatusCode;
import com.sideproject.domain.repository.AdminRepository;
import com.sideproject.exception.AccountException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static com.sideproject.domain.enums.AdminStatusCode.LOCK;
import static com.sideproject.domain.enums.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService{

  private final int MAX_LOGIN_FAIL_COUNT = 5;

  private final AdminRepository adminRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final CProperties cProperties;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AdminEntity adminEntity = adminRepository.findByEmail(username);

    if(adminEntity == null){
      throw new AccountException(USER_NOT_EXIST);
    }else if(adminEntity.getStatus().equals(LOCK)){
      throw new AccountException(LOCKED_ACCOUNT);
    }else if(adminEntity.getLoginFailCount() >= MAX_LOGIN_FAIL_COUNT){
      adminRepository.updateStatus(LOCK, adminEntity.getEmail());
      throw new AccountException(LOCKED_ACCOUNT);
    }

    return new User(adminEntity.getEmail(), adminEntity.getPassword(),
        true, true, true, true,
        new ArrayList<>());
  }

  @Override
  public AdminResponseDto getAdminDetailsByEmail(String email) {
    AdminEntity adminEntity = adminRepository.findByEmail(email);

    if(adminEntity == null){
      throw new UsernameNotFoundException(email);
    }

    AdminResponseDto adminResponseDto = adminEntity.toDto();
    return adminResponseDto;
  }

  @Override
  public AdminResponseDto getAdmin(AdminRequestDto adminRequestDto) {
    AdminEntity adminEntity = Optional.ofNullable(adminRepository.findByAdminIdAndStatus(adminRequestDto.getAdminId(), AdminStatusCode.USE))
        .orElseThrow(() -> new AccountException(USER_NOT_EXIST)).get();

    if (adminEntity == null){
      throw new AccountException(USER_NOT_EXIST);
    }

    return adminEntity.toDto();
  }

  @Override
  public AdminResponseDto createAdmin(AdminCreateRequestDto adminCreateRequestDto) {
    this.isDuplicateUserEmail(adminCreateRequestDto.getEmail());

    AdminEntity adminEntity = new AdminEntity().builder()
        .email(adminCreateRequestDto.getEmail())
        .name(adminCreateRequestDto.getName())
        .password(new BCryptPasswordEncoder().encode(adminCreateRequestDto.getPassword()))
        .status(AdminStatusCode.USE)
        .build();

    adminRepository.save(adminEntity);

    AdminResponseDto adminResponseDto = adminEntity.toDto();
    return adminResponseDto;
  }

  @Override
  public void logout(HttpServletResponse response) {
    jwtTokenProvider.resetAccessCookie(response, cProperties.getJwt().getAccessHeader());
    jwtTokenProvider.resetRefreshCookie(response, cProperties.getJwt().getRefreshHeader());
  }

  @Override
  public void increaseFailCount(String email) {
    adminRepository.incrementFailCount(email);
  }

  @Override
  public void resetFailCount(String email) {
    adminRepository.resetFailCount(email);
  }

  private void isDuplicateUserEmail(String userEmail){
    AdminEntity userEntity = adminRepository.findByEmail(userEmail);

    if(userEntity != null){
      throw new AccountException(DUPLICATED_USER);
    }
  }
}

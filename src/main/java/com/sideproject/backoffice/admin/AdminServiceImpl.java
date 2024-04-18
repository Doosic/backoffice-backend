package com.sideproject.backoffice.admin;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.common.CProperties;
import com.sideproject.config.jwt.JwtTokenProvider;
import com.sideproject.domain.dto.admin.AdminCreateRequestDto;
import com.sideproject.domain.dto.admin.AdminRequestDto;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import com.sideproject.domain.dto.admin.AdminUpdateRequestDto;
import com.sideproject.domain.entity.AdminEntity;
import com.sideproject.domain.entity.QAdminEntity;
import com.sideproject.domain.enums.AdminStatusCode;
import com.sideproject.domain.repository.AdminRepository;
import com.sideproject.exception.APIException;
import com.sideproject.exception.AccountException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sideproject.domain.enums.AdminStatusCode.LOCK;
import static com.sideproject.domain.enums.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService{

  private final int MAX_LOGIN_FAIL_COUNT = 5;

  @PersistenceContext
  private EntityManager em;
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

    return adminEntity.toDto();
  }

  @Override
  public Page<AdminResponseDto> getAdmins(AdminRequestDto adminRequestDto) {
    PageRequest pageRequest = PageRequest.of(adminRequestDto.getPageNum() - 1, adminRequestDto.getPageRowCount());

    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QAdminEntity adminEntity = QAdminEntity.adminEntity;

    StringTemplate dateTime = Expressions.stringTemplate("TO_CHAR({0}, {1})", adminEntity.createDate, "YYYY-MM-DD HH24:MI:SS");

    JPAQuery<AdminResponseDto> jpaQuery = queryFactory.select(Projections.fields(AdminResponseDto.class,
        adminEntity.adminId,
        adminEntity.email,
        adminEntity.name,
        adminEntity.status,
        dateTime.as("createDate")
        )).from(adminEntity)
        .orderBy(adminEntity.adminId.desc())
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .where(adminEntity.status.ne(AdminStatusCode.DELETE));

    JPAQuery<Long> countQuery = queryFactory
        .select(adminEntity.count())
        .from(adminEntity)
        .where(adminEntity.status.ne(AdminStatusCode.DELETE));

    if(adminRequestDto.getSearchTitle() != null){
      switch (adminRequestDto.getSearchTitle()){
        case "email" -> {
          jpaQuery.where(adminEntity.email.contains(adminRequestDto.getSearchText()));
          countQuery.where(adminEntity.email.contains(adminRequestDto.getSearchText()));
        }
        case "name" -> {
          jpaQuery.where(adminEntity.name.contains(adminRequestDto.getSearchText()));
          countQuery.where(adminEntity.name.contains(adminRequestDto.getSearchText()));
        }
        case "status" -> {
          jpaQuery.where(adminEntity.status.eq(AdminStatusCode.valueOf(adminRequestDto.getSearchText())));
          countQuery.where(adminEntity.status.eq(AdminStatusCode.valueOf(adminRequestDto.getSearchText())));
        }
      }
    }

    List<AdminResponseDto> admins = jpaQuery.fetch();

    return PageableExecutionUtils.getPage(admins, pageRequest, countQuery::fetchCount);
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

  @Transactional
  @Override
  public AdminResponseDto updateAdmin(AdminUpdateRequestDto adminUpdateRequestDto) {
    AdminEntity adminEntity = Optional.ofNullable(adminRepository.findByAdminIdAndStatus(adminUpdateRequestDto.getAdminId(), AdminStatusCode.USE))
        .orElseThrow(() -> new AccountException(USER_NOT_EXIST)).get();

    AdminResponseDto adminResponseDto = adminUpdateRequestDto.toEntity(adminEntity).toDto();
    return adminResponseDto;
  }

  @Transactional
  @Override
  public AdminResponseDto deleteAdmin(AdminRequestDto adminRequestDto) {
    AdminEntity adminEntity = Optional.ofNullable(adminRepository.findByAdminIdAndStatus(adminRequestDto.getAdminId(), AdminStatusCode.USE))
        .orElseThrow(() -> new AccountException(USER_NOT_EXIST)).get();

    adminEntity.setStatus(AdminStatusCode.DELETE);
    AdminResponseDto adminResponseDto = adminEntity.toDto();
    return adminResponseDto;
  }

  @Transactional
  @Override
  public AdminResponseDto unlockAdmin(AdminRequestDto adminRequestDto) {
    AdminEntity adminEntity = Optional.ofNullable(adminRepository.findByAdminIdAndStatus(adminRequestDto.getAdminId(), AdminStatusCode.LOCK))
        .orElseThrow(() -> new AccountException(USER_NOT_EXIST)).get();

    adminEntity.setStatus(AdminStatusCode.USE);
    AdminResponseDto adminResponseDto = adminEntity.toDto();
    return adminResponseDto;
  }

  @Override
  public void logout(HttpServletResponse response) {
    try{
      jwtTokenProvider.resetAccessCookie(response, cProperties.getJwt().getAccessHeader());
      jwtTokenProvider.resetRefreshCookie(response, cProperties.getJwt().getRefreshHeader());
    } catch (Exception e){
      throw new APIException(LOGOUT_FAIL);
    }
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

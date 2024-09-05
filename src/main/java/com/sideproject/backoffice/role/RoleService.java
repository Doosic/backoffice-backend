package com.sideproject.backoffice.role;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.domain.dto.admin.AdminSimpleResponseDto;
import com.sideproject.domain.dto.role.*;
import com.sideproject.domain.entity.*;
import com.sideproject.domain.enums.RoleType;
import com.sideproject.domain.repository.*;
import com.sideproject.exception.APIException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sideproject.domain.enums.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class RoleService {

  @PersistenceContext
  private EntityManager em;
  private final RoleRepository roleRepository;
  private final RoleMenuRepository roleMenuRepository;
  private final RoleAuthRepository roleAuthRepository;
  private final AuthRepository authRepository;

  public Page<RoleResponseDto> getAuths(RoleRequestDto roleRequestDto){
    PageRequest pageRequest = PageRequest.of(roleRequestDto.getPageNum() - 1, roleRequestDto.getPageRowCount());

    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QRoleEntity roleEntity = QRoleEntity.roleEntity;
    QAdminEntity adminEntity = QAdminEntity.adminEntity;

    StringTemplate dateTime = Expressions.stringTemplate("TO_CHAR({0}, {1})", roleEntity.createDate, "YYYY-MM-DD HH24:MI:SS");

    JPAQuery<RoleResponseDto> jpaQuery = queryFactory.select(Projections.fields(RoleResponseDto.class,
        roleEntity.roleId,
        roleEntity.authName,
        roleEntity.roleType,
        dateTime.as("createDate"),
        Projections.fields(AdminSimpleResponseDto.class,
            adminEntity.name
            ).as("regUser")
        ))
        .from(roleEntity)
        .leftJoin(adminEntity).on(roleEntity.regUser.eq(adminEntity.adminId))
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .orderBy(roleEntity.roleId.desc());

    JPAQuery<Long> countQuery = queryFactory
        .select(roleEntity.count())
        .from(roleEntity);

    if(roleRequestDto.getSearchTitle() != null){
      switch (roleRequestDto.getSearchTitle()){
        case "name" -> {
          jpaQuery.where(roleEntity.authName.contains(roleRequestDto.getSearchText()));
          countQuery.where(roleEntity.authName.contains(roleRequestDto.getSearchText()));
        }
        case "type" -> {
          jpaQuery.where(roleEntity.roleType.eq(RoleType.valueOf(roleRequestDto.getSearchText())));
          countQuery.where(roleEntity.roleType.eq(RoleType.valueOf(roleRequestDto.getSearchText())));
        }
      }
    }

    List<RoleResponseDto> auths = jpaQuery.fetch();

    return PageableExecutionUtils.getPage(auths, pageRequest, countQuery::fetchCount);
  }

  @Transactional
  public RoleResponseDto createAuthAndMenu(
      Long adminId,
      RoleMenuCreateRequestDto roleMenuCreateRequestDto
  ) {
    this.isDuplicateAuthName(roleMenuCreateRequestDto.getAuthName());

    RoleEntity roleEntity = new RoleEntity().builder()
        .authName(roleMenuCreateRequestDto.getAuthName())
        .roleType(RoleType.MENU)
        .regUser(adminId)
        .build();

    roleRepository.save(roleEntity);

    RoleResponseDto roleResponseDto = roleEntity.toDto();

    for(Long key : roleMenuCreateRequestDto.getMenuKeys()){
      RoleMenuEntity menu = new RoleMenuEntity().builder()
          .roleId(roleResponseDto.getRoleId())
          .menuId(key)
          .build();
      roleMenuRepository.save(menu);
    }

    return roleResponseDto;
  }

  @Transactional
  public RoleResponseDto createAuthAndFunc(
      Long adminId,
      RoleAuthCreateRequestDto roleAuthCreateRequestDto
  ) {
    this.isDuplicateAuthName(roleAuthCreateRequestDto.getAuthName());

    RoleEntity roleEntity = new RoleEntity().builder()
        .authName(roleAuthCreateRequestDto.getAuthName())
        .roleType(RoleType.AUTH)
        .regUser(adminId)
        .build();

    roleRepository.save(roleEntity);

    List<AuthEntity> functions = authRepository.findAll();
    Map<Long, String> funcMap = new HashMap<>();

    for (AuthEntity func : functions){
      funcMap.put(func.getAuthId(), func.getAuthName());
    }

    RoleResponseDto roleResponseDto = roleEntity.toDto();

    for(Long key : roleAuthCreateRequestDto.getFuncKeys()){
      RoleAuthEntity menu = new RoleAuthEntity().builder()
          .roleId(roleResponseDto.getRoleId())
          .authId(key)
          .authName(funcMap.get(key))
          .build();
      roleAuthRepository.save(menu);
    }

    return roleResponseDto;
  }

  @Transactional
  public RoleResponseDto updateAuthAndMenu(
      Long adminId,
      RoleMenuUpdateRequest roleMenuUpdateRequest
  ) {

    RoleEntity roleEntity = Optional.ofNullable(roleRepository.findById(roleMenuUpdateRequest.getRoleId()))
        .orElseThrow(() -> new APIException(DATA_NOT_EXIST)).get();

    roleMenuRepository.deleteByRoleId(roleMenuUpdateRequest.getRoleId());

    for(Long key : roleMenuUpdateRequest.getMenuKeys()){
      RoleMenuEntity menu = new RoleMenuEntity().builder()
          .roleId(roleEntity.getRoleId())
          .menuId(key)
          .build();
      roleMenuRepository.save(menu);
    }

    RoleResponseDto roleResponseDto = roleEntity.toDto();

    return roleResponseDto;
  }

  @Transactional
  public RoleResponseDto updateAuthAndFunc(
      Long adminId,
      RoleAuthUpdateRequest roleAuthUpdateRequest
  ) {

    RoleEntity roleEntity = Optional.ofNullable(roleRepository.findById(roleAuthUpdateRequest.getRoleId()))
        .orElseThrow(() -> new APIException(DATA_NOT_EXIST)).get();

    roleAuthRepository.deleteByRoleId(roleAuthUpdateRequest.getRoleId());

    List<AuthEntity> functions = authRepository.findAll();
    Map<Long, String> authMap = new HashMap<>();

    for (AuthEntity func : functions){
      authMap.put(func.getAuthId(), func.getAuthName());
    }

    for(Long key : roleAuthUpdateRequest.getAuthKeys()){
      RoleAuthEntity menu = new RoleAuthEntity().builder()
          .roleId(roleEntity.getRoleId())
          .authId(key)
          .authName(authMap.get(key))
          .build();
      roleAuthRepository.save(menu);
    }

    RoleResponseDto roleResponseDto = roleEntity.toDto();

    return roleResponseDto;
  }

  private void isDuplicateAuthName(String authName){
    RoleEntity roleEntity = roleRepository.findByAuthName(authName);

    if(roleEntity != null){
      throw new APIException(DUPLICATED_DATA);
    }
  }
}

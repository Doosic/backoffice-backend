package com.sideproject.backoffice.auth;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.domain.dto.admin.AdminInfo;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import com.sideproject.domain.dto.admin.AdminSimpleResponseDto;
import com.sideproject.domain.dto.auth.AuthMenuCreateRequestDto;
import com.sideproject.domain.dto.auth.AuthRequestDto;
import com.sideproject.domain.dto.auth.AuthResponseDto;
import com.sideproject.domain.entity.*;
import com.sideproject.domain.enums.AdminStatusCode;
import com.sideproject.domain.enums.AuthType;
import com.sideproject.domain.repository.AuthMenuRepository;
import com.sideproject.domain.repository.AuthRepository;
import com.sideproject.domain.repository.MenuRepository;
import com.sideproject.exception.APIException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sideproject.domain.enums.ErrorCode.DUPLICATED_DATA;

@RequiredArgsConstructor
@Service
public class AuthService {

  @PersistenceContext
  private EntityManager em;
  private final AuthRepository authRepository;
  private final AuthMenuRepository authMenuRepository;

  public Page<AuthResponseDto> getAuths(AuthRequestDto authRequestDto){
    PageRequest pageRequest = PageRequest.of(authRequestDto.getPageNum() - 1, authRequestDto.getPageRowCount());

    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QAuthEntity authEntity = QAuthEntity.authEntity;
    QAdminEntity adminEntity = QAdminEntity.adminEntity;

    StringTemplate dateTime = Expressions.stringTemplate("TO_CHAR({0}, {1})", authEntity.createDate, "YYYY-MM-DD HH24:MI:SS");

    JPAQuery<AuthResponseDto> jpaQuery = queryFactory.select(Projections.fields(AuthResponseDto.class,
        authEntity.authId,
        authEntity.authName,
        authEntity.authType,
        dateTime.as("createDate"),
        Projections.fields(AdminSimpleResponseDto.class,
            adminEntity.name
            ).as("regUser")
        ))
        .from(authEntity)
        .leftJoin(adminEntity).on(authEntity.regUser.eq(adminEntity.adminId))
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .orderBy(authEntity.authId.desc());

    JPAQuery<Long> countQuery = queryFactory
        .select(authEntity.count())
        .from(authEntity);

    if(authRequestDto.getSearchTitle() != null){
      switch (authRequestDto.getSearchTitle()){
        case "name" -> {
          jpaQuery.where(authEntity.authName.contains(authRequestDto.getSearchText()));
          countQuery.where(authEntity.authName.contains(authRequestDto.getSearchText()));
        }
        case "type" -> {
          jpaQuery.where(authEntity.authType.eq(AuthType.valueOf(authRequestDto.getSearchText())));
          countQuery.where(authEntity.authType.eq(AuthType.valueOf(authRequestDto.getSearchText())));
        }
      }
    }

    List<AuthResponseDto> auths = jpaQuery.fetch();

    return PageableExecutionUtils.getPage(auths, pageRequest, countQuery::fetchCount);
  }

  @Transactional
  public AuthResponseDto createAuthAndMenu(
      Long adminId,
      AuthMenuCreateRequestDto authMenuCreateRequestDto
  ) {
    this.isDuplicateAuthName(authMenuCreateRequestDto.getAuthName());

    AuthEntity authEntity = new AuthEntity().builder()
        .authName(authMenuCreateRequestDto.getAuthName())
        .authType(AuthType.MENU)
        .regUser(adminId)
        .build();

    authRepository.save(authEntity);

    AuthResponseDto authResponseDto = authEntity.toDto();

    for(Long key : authMenuCreateRequestDto.getMenuKeys()){
      AuthMenuEntity menu = new AuthMenuEntity().builder()
          .authId(authResponseDto.getAuthId())
          .menuId(key)
          .build();
      authMenuRepository.save(menu);
    }

    return authResponseDto;
  }

  private void isDuplicateAuthName(String authName){
    AuthEntity authEntity = authRepository.findByAuthName(authName);

    if(authEntity != null){
      throw new APIException(DUPLICATED_DATA);
    }
  }
}

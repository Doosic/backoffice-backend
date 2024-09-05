package com.sideproject.backoffice.auth;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.domain.dto.auth.AuthResponseDto;
import com.sideproject.domain.dto.auth.AuthSimpleResponseDto;
import com.sideproject.domain.entity.AuthEntity;
import com.sideproject.domain.entity.QAuthEntity;
import com.sideproject.domain.entity.QRoleAuthEntity;
import com.sideproject.domain.repository.AuthRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

  @PersistenceContext
  private EntityManager em;
  private final AuthRepository authRepository;

  public List<AuthResponseDto> getAuths() {
    List<AuthResponseDto> allFunctuins = authRepository.findAllByOrderByAuthIdAsc()
        .stream()
        .map(AuthEntity::toDto)
        .collect(Collectors.toList());

    Map<Long, AuthResponseDto> funcMap = new HashMap<>();
    List<AuthResponseDto> roots = new ArrayList<>();

    for (AuthResponseDto func : allFunctuins){
      func.setChildren(new ArrayList<>());
      funcMap.put(func.getKey(), func);
    }

    for (AuthResponseDto func : allFunctuins){
      Long parentId = func.getFuncParent();
      if (parentId != -1){
        AuthResponseDto parentFunc = funcMap.get(parentId);
        if (parentFunc != null){
          parentFunc.getChildren().add(func);
        }
      } else {
        roots.add(func);
      }
    }

    return roots;
  }

  public List<AuthSimpleResponseDto> getAuthKeys(Long roleId){
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QRoleAuthEntity roleAuthEntity = QRoleAuthEntity.roleAuthEntity;
    QAuthEntity authEntity = QAuthEntity.authEntity;

    JPAQuery<AuthSimpleResponseDto> jpaQuery = queryFactory.select(Projections.fields(AuthSimpleResponseDto.class,
                    authEntity.authId.as("key")
        )).from(roleAuthEntity)
        .leftJoin(authEntity)
        .on(roleAuthEntity.authId.eq(authEntity.authId))
        .where(roleAuthEntity.roleId.eq(roleId));

    List<AuthSimpleResponseDto> allFunctions = jpaQuery.fetch();

    return allFunctions;
  }

  public List<AuthResponseDto> getAuthFuncs(Long roleId) {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QRoleAuthEntity roleAuthEntity = QRoleAuthEntity.roleAuthEntity;
    QAuthEntity authEntity = QAuthEntity.authEntity;

    JPAQuery<AuthResponseDto> jpaQuery = queryFactory.select(Projections.fields(AuthResponseDto.class,
                    authEntity.authId.as("key"),
                    authEntity.authParent,
                    authEntity.authName.as("label"),
                    authEntity.authIcon.as("icon")
        )).from(roleAuthEntity)
        .leftJoin(authEntity)
        .on(roleAuthEntity.authId.eq(authEntity.authId))
        .where(roleAuthEntity.roleId.eq(roleId));

    List<AuthResponseDto> allFuncs = jpaQuery.fetch();

    Map<Long, AuthResponseDto> funcMap = new HashMap<>();
    List<AuthResponseDto> roots = new ArrayList<>();

    for (AuthResponseDto auth : allFuncs){
      auth.setChildren(new ArrayList<>());
      funcMap.put(auth.getKey(), auth);
    }

    for (AuthResponseDto func : allFuncs){
      Long parentId = func.getFuncParent();
      if (parentId != -1){
        AuthResponseDto parentMenu = funcMap.get(parentId);
        if (parentMenu != null){
          parentMenu.getChildren().add(func);
        }
      } else {
        roots.add(func);
      }
    }

    return roots;
  }
}
package com.sideproject.backoffice.function;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.domain.dto.function.FunctionRequestDto;
import com.sideproject.domain.dto.function.FunctionResponseDto;
import com.sideproject.domain.dto.function.FunctionSimpleResponseDto;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import com.sideproject.domain.entity.FunctionEntity;
import com.sideproject.domain.entity.QAuthFuncEntity;
import com.sideproject.domain.entity.QFunctionEntity;
import com.sideproject.domain.entity.QMenuEntity;
import com.sideproject.domain.repository.FunctionRepository;
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
public class FunctionService {

  @PersistenceContext
  private EntityManager em;
  private final FunctionRepository functionRepository;

  public List<FunctionResponseDto> getFunctions() {
    List<FunctionResponseDto> allFunctuins = functionRepository.findAllByOrderByFuncIdAsc()
        .stream()
        .map(FunctionEntity::toDto)
        .collect(Collectors.toList());

    Map<Long, FunctionResponseDto> funcMap = new HashMap<>();
    List<FunctionResponseDto> roots = new ArrayList<>();

    for (FunctionResponseDto func : allFunctuins){
      func.setChildren(new ArrayList<>());
      funcMap.put(func.getKey(), func);
    }

    for (FunctionResponseDto func : allFunctuins){
      Long parentId = func.getFuncParent();
      if (parentId != -1){
        FunctionResponseDto parentFunc = funcMap.get(parentId);
        if (parentFunc != null){
          parentFunc.getChildren().add(func);
        }
      } else {
        roots.add(func);
      }
    }

    return roots;
  }

  public List<FunctionSimpleResponseDto> getFuncKeys(Long funcId){
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QAuthFuncEntity authFuncEntity = QAuthFuncEntity.authFuncEntity;
    QFunctionEntity functionEntity = QFunctionEntity.functionEntity;

    JPAQuery<FunctionSimpleResponseDto> jpaQuery = queryFactory.select(Projections.fields(FunctionSimpleResponseDto.class,
          functionEntity.funcId.as("key")
        )).from(authFuncEntity)
        .leftJoin(functionEntity)
        .on(authFuncEntity.funcId.eq(functionEntity.funcId))
        .where(authFuncEntity.authId.eq(funcId));

    List<FunctionSimpleResponseDto> allFunctions = jpaQuery.fetch();

    return allFunctions;
  }

  public List<FunctionResponseDto> getAuthFuncs(Long funcId) {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QAuthFuncEntity authFuncEntity = QAuthFuncEntity.authFuncEntity;
    QFunctionEntity functionEntity = QFunctionEntity.functionEntity;

    JPAQuery<FunctionResponseDto> jpaQuery = queryFactory.select(Projections.fields(FunctionResponseDto.class,
        functionEntity.funcId.as("key"),
        functionEntity.funcParent,
        functionEntity.funcName.as("label"),
        functionEntity.funcIcon.as("icon")
        )).from(authFuncEntity)
        .leftJoin(functionEntity)
        .on(authFuncEntity.funcId.eq(functionEntity.funcId))
        .where(authFuncEntity.authId.eq(funcId));

    List<FunctionResponseDto> allFuncs = jpaQuery.fetch();

    Map<Long, FunctionResponseDto> funcMap = new HashMap<>();
    List<FunctionResponseDto> roots = new ArrayList<>();

    for (FunctionResponseDto auth : allFuncs){
      auth.setChildren(new ArrayList<>());
      funcMap.put(auth.getKey(), auth);
    }

    for (FunctionResponseDto func : allFuncs){
      Long parentId = func.getFuncParent();
      if (parentId != -1){
        FunctionResponseDto parentMenu = funcMap.get(parentId);
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
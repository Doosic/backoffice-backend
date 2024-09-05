package com.sideproject.backoffice.menu;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import com.sideproject.domain.dto.menu.MenuSimpleResponseDto;
import com.sideproject.domain.entity.MenuEntity;
import com.sideproject.domain.entity.QMenuEntity;
import com.sideproject.domain.entity.QRoleMenuEntity;
import com.sideproject.domain.repository.AdminRepository;
import com.sideproject.domain.repository.MenuRepository;
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
public class MenuService {

  @PersistenceContext
  private EntityManager em;
  private final MenuRepository menuRepository;

  public List<MenuResponseDto> getMenus() {
    List<MenuResponseDto> allMenus = menuRepository.findAll()
        .stream()
        .map(MenuEntity::toDto)
        .collect(Collectors.toList());

    Map<Long, MenuResponseDto> menuMap = new HashMap<>();
    List<MenuResponseDto> roots = new ArrayList<>();

    for (MenuResponseDto menu : allMenus){
      menu.setChildren(new ArrayList<>());
      menuMap.put(menu.getKey(), menu);
    }

    for (MenuResponseDto menu : allMenus){
      Long parentId = menu.getMenuParent();
      if (parentId != -1){
        MenuResponseDto parentMenu = menuMap.get(parentId);
        if (parentMenu != null){
          parentMenu.getChildren().add(menu);
        }
      } else {
        roots.add(menu);
      }
    }

    return roots;
  }

  public List<MenuResponseDto> getAuthMenus(Long menuId) {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QRoleMenuEntity roleMenuEntity = QRoleMenuEntity.roleMenuEntity;
    QMenuEntity menuEntity = QMenuEntity.menuEntity;

    JPAQuery<MenuResponseDto> jpaQuery = queryFactory.select(Projections.fields(MenuResponseDto.class,
        menuEntity.menuId.as("key"),
        menuEntity.menuParent,
        menuEntity.menuName.as("label"),
        menuEntity.menuIcon.as("icon"),
        menuEntity.menuLink.as("to"),
        menuEntity.menuQuery.as("query")
        )).from(roleMenuEntity)
        .leftJoin(menuEntity)
        .on(roleMenuEntity.menuId.eq(menuEntity.menuId))
        .where(roleMenuEntity.roleId.eq(menuId));

    List<MenuResponseDto> allMenus = jpaQuery.fetch();

    Map<Long, MenuResponseDto> menuMap = new HashMap<>();
    List<MenuResponseDto> roots = new ArrayList<>();

    for (MenuResponseDto menu : allMenus){
      menu.setChildren(new ArrayList<>());
      menuMap.put(menu.getKey(), menu);
    }

    for (MenuResponseDto menu : allMenus){
      Long parentId = menu.getMenuParent();
      if (parentId != -1){
        MenuResponseDto parentMenu = menuMap.get(parentId);
        if (parentMenu != null){
          parentMenu.getChildren().add(menu);
        }
      } else {
        roots.add(menu);
      }
    }

    return roots;
  }

  public List<MenuSimpleResponseDto> getMenuKeys(Long menuId) {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QRoleMenuEntity roleMenuEntity = QRoleMenuEntity.roleMenuEntity;
    QMenuEntity menuEntity = QMenuEntity.menuEntity;

    JPAQuery<MenuSimpleResponseDto> jpaQuery = queryFactory.select(Projections.fields(MenuSimpleResponseDto.class,
            menuEntity.menuId.as("key")
        )).from(roleMenuEntity)
        .leftJoin(menuEntity)
        .on(roleMenuEntity.menuId.eq(menuEntity.menuId))
        .where(roleMenuEntity.roleId.eq(menuId));

    List<MenuSimpleResponseDto> allMenus = jpaQuery.fetch();

    return allMenus;
  }
}

package com.sideproject.backoffice.menu;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.domain.dto.menu.MenuResponseDto;
import com.sideproject.domain.entity.MenuEntity;
import com.sideproject.domain.entity.QAuthMenuEntity;
import com.sideproject.domain.entity.QMenuEntity;
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
      menu.setItems(new ArrayList<>());
      menuMap.put(menu.getMenuId(), menu);
    }

    for (MenuResponseDto menu : allMenus){
      Long parentId = menu.getMenuParent();
      if (parentId != -1){
        MenuResponseDto parentMenu = menuMap.get(parentId);
        if (parentMenu != null){
          parentMenu.getItems().add(menu);
        }
      } else {
        roots.add(menu);
      }
    }

    return roots;
  }

  public List<MenuResponseDto> getAuthMenus(Long authId) {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QAuthMenuEntity authMenuEntity = QAuthMenuEntity.authMenuEntity;
    QMenuEntity menuEntity = QMenuEntity.menuEntity;

    JPAQuery<MenuResponseDto> jpaQuery = queryFactory.select(Projections.fields(MenuResponseDto.class,
        menuEntity.menuId,
        menuEntity.menuParent,
        menuEntity.menuName.as("label"),
        menuEntity.menuIcon.as("icon"),
        menuEntity.menuLink.as("to"),
        menuEntity.menuQuery.as("query")
        )).from(authMenuEntity)
        .leftJoin(menuEntity)
        .on(authMenuEntity.menuId.eq(menuEntity.menuId))
        .where(authMenuEntity.authId.eq(authId));

    List<MenuResponseDto> allMenus = jpaQuery.fetch();

    Map<Long, MenuResponseDto> menuMap = new HashMap<>();
    List<MenuResponseDto> roots = new ArrayList<>();

    for (MenuResponseDto menu : allMenus){
      menu.setItems(new ArrayList<>());
      menuMap.put(menu.getMenuId(), menu);
    }

    for (MenuResponseDto menu : allMenus){
      Long parentId = menu.getMenuParent();
      if (parentId != -1){
        MenuResponseDto parentMenu = menuMap.get(parentId);
        if (parentMenu != null){
          parentMenu.getItems().add(menu);
        }
      } else {
        roots.add(menu);
      }
    }

    return roots;
  }
}

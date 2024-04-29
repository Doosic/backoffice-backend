package com.sideproject.domain.repository;

import com.sideproject.domain.entity.MenuEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends CrudRepository<MenuEntity, Long> {

  List<MenuEntity> findAll();
}

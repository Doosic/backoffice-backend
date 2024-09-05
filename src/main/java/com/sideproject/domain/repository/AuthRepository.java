package com.sideproject.domain.repository;

import com.sideproject.domain.entity.AuthEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthRepository extends CrudRepository<AuthEntity, Long> {

  List<AuthEntity> findAllByOrderByAuthIdAsc();
  List<AuthEntity> findAll();
}
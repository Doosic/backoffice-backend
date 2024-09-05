package com.sideproject.domain.repository;

import com.sideproject.domain.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

  RoleEntity findByAuthName(String authName);
}

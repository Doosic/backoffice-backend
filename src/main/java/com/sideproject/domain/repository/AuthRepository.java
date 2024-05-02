package com.sideproject.domain.repository;

import com.sideproject.domain.entity.AuthEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepository extends CrudRepository<AuthEntity, Long> {

  AuthEntity findByAuthName(String authName);
}

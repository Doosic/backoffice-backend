package com.sideproject.domain.repository;

import com.sideproject.domain.entity.AuthFuncEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AuthFuncRepository extends CrudRepository<AuthFuncEntity, Long> {

  @Modifying
  @Transactional
  @Query("DELETE FROM AuthFuncEntity au WHERE au.authId = :authId")
  void deleteByAuthId(@Param("authId") Long authId);
}

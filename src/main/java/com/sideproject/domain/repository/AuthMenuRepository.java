package com.sideproject.domain.repository;

import com.sideproject.domain.entity.AuthMenuEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AuthMenuRepository extends CrudRepository<AuthMenuEntity, Long> {


  @Modifying
  @Transactional
  @Query("DELETE FROM AuthMenuEntity au WHERE au.authId = :authId")
  void deleteByAuthId(@Param("authId") Long authId);
}

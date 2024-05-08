package com.sideproject.domain.repository;

import com.sideproject.domain.entity.AuthFuncEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AuthFuncRepository extends CrudRepository<AuthFuncEntity, Long> {

  @Query("SELECT baf FROM AuthFuncEntity baf LEFT OUTER JOIN FunctionEntity bf ON (baf.funcId = bf.funcId) " +
      "WHERE baf.authId = :authId AND bf.funcName = :funcName")
  AuthFuncEntity findByFuncNameAndAuthId(@Param("authId") Long authId, @Param("funcName") String funcName);

  @Modifying
  @Transactional
  @Query("DELETE FROM AuthFuncEntity au WHERE au.authId = :authId")
  void deleteByAuthId(@Param("authId") Long authId);
}

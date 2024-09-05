package com.sideproject.domain.repository;

import com.sideproject.domain.entity.RoleAuthEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RoleAuthRepository extends CrudRepository<RoleAuthEntity, Long> {

  @Query("SELECT baf FROM RoleAuthEntity baf LEFT OUTER JOIN AuthEntity bf ON (baf.authId = bf.authId) " +
      "WHERE baf.roleId = :roleId AND bf.authName = :authName")
  RoleAuthEntity findByAuthNameAndRoleId(@Param("roleId") Long roleId, @Param("authName") String authName);

  @Modifying
  @Transactional
  @Query("DELETE FROM RoleAuthEntity au WHERE au.roleId = :roleId")
  void deleteByRoleId(@Param("roleId") Long roleId);
}

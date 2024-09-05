package com.sideproject.domain.repository;

import com.sideproject.domain.entity.RoleMenuEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RoleMenuRepository extends CrudRepository<RoleMenuEntity, Long> {


  @Modifying
  @Transactional
  @Query("DELETE FROM RoleMenuEntity au WHERE au.roleId = :roleId")
  void deleteByRoleId(@Param("roleId") Long roleId);
}

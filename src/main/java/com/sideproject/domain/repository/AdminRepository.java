package com.sideproject.domain.repository;

import com.sideproject.domain.entity.AdminEntity;
import com.sideproject.domain.enums.AdminStatusCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AdminRepository extends CrudRepository<AdminEntity, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE AdminEntity u SET u.loginFailCount = u.loginFailCount + 1 WHERE u.email = :email")
  void incrementFailCount(@Param("email") String email);

  @Modifying
  @Transactional
  @Query("UPDATE AdminEntity u SET u.loginFailCount = 0, u.lockDate = null WHERE u.email = :email")
  void resetFailCount(@Param("email") String email);

  @Modifying
  @Transactional
  @Query("UPDATE AdminEntity u SET u.status = :status, u.lockDate = CURRENT_TIMESTAMP  WHERE u.email = :email")
  void updateStatus(
      @Param("status") AdminStatusCode status,
      @Param("email") String email
  );
}

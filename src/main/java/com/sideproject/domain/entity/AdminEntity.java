package com.sideproject.domain.entity;

import com.sideproject.common.BaseTimeEntity;
import com.sideproject.common.Utils;
import com.sideproject.domain.dto.admin.AdminResponseDto;
import com.sideproject.domain.enums.AdminStatusCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "bk_admin")
public class AdminEntity extends BaseTimeEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "admin_id")
  private Long adminId;

  @Column(name = "menu_id")
  private Long menuId;

  @Column(name = "auth_id")
  private Long authId;

  @Column(nullable = false, length = 50)
  private String email;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, unique = true, name="password")
  private String password;

  @ColumnDefault("0")
  @Column(nullable = false, name="login_fail_count")
  private int loginFailCount;

  @Column(nullable = true, name="lock_date")
  private LocalDateTime lockDate;

  @Column(nullable = true, name="delete_date")
  private LocalDateTime deleteDate;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AdminStatusCode status = AdminStatusCode.USE;

  public AdminEntity() {}

  public AdminResponseDto toDto() {
    AdminResponseDto dto = new AdminResponseDto();
    dto.setAdminId(this.getAdminId());
    dto.setEmail(this.getEmail());
    dto.setName(this.getName());
    dto.setStatus(this.getStatus());
    dto.setMenuId(this.getMenuId());
    dto.setAuthId(this.getAuthId());
    dto.setCreateDate(Utils.getDateFormatString(this.getCreateDate()));
    return dto;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}

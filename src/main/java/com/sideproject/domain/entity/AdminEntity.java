package com.sideproject.domain.entity;

import com.sideproject.common.BaseTimeEntity;
import com.sideproject.domain.enums.AdminStatusCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
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

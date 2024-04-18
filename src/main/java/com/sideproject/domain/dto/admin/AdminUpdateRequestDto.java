package com.sideproject.domain.dto.admin;

import com.sideproject.domain.entity.AdminEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUpdateRequestDto {

  @NotNull(message = "AdminId cannot be null")
  private Long adminId;

  @NotNull(message = "Name cannot be null")
  @Size(min = 3, message = "Name not be less than two characters")
  private String name;

  public AdminEntity toEntity(AdminEntity entity){
    if(this.getName() != null) {entity.setName(this.getName());}
    return entity;
  }

}

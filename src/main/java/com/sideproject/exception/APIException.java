package com.sideproject.exception;

import com.sideproject.domain.enums.ErrorCode;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException{

  public APIException(ErrorCode errorCode) {
    super(errorCode.getMessage());
  }
}

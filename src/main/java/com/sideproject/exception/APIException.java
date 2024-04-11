package com.sideproject.exception;

import com.sideproject.domain.enums.ErrorCode;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException{

  private final ErrorCode errorCode;

  public APIException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}

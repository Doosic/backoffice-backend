package com.sideproject.exception;

import com.sideproject.domain.enums.ErrorCode;

public class AccountException extends RuntimeException {
  public AccountException(ErrorCode errorCode) {
    super(errorCode.getMessage());
  }
}

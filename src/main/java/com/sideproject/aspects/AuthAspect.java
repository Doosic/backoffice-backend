package com.sideproject.aspects;

import com.sideproject.annotation.BeforeRequiresAuthorization;
import com.sideproject.common.BaseController;
import com.sideproject.domain.entity.AuthFuncEntity;
import com.sideproject.domain.enums.ErrorCode;
import com.sideproject.domain.repository.AuthFuncRepository;
import com.sideproject.exception.APIException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 권한 확인 관련 AOP 구성
 *
 * @author : doosic
 * @fileName : AuthAspect
 * */
@Aspect
@Component
public class AuthAspect extends BaseController{

  private final AuthFuncRepository authFuncRepository;

  @Autowired
  public AuthAspect(AuthFuncRepository authFuncRepository){
    this.authFuncRepository = authFuncRepository;
  }

  @Before("@annotation(requiresAuthorization)")
  public void checkAuthorization(BeforeRequiresAuthorization requiresAuthorization) {
    String authorization = requiresAuthorization.value();
    AuthFuncEntity auth = authFuncRepository.findByFuncNameAndAuthId(this.getSessionInfo().getFuncId(), authorization);

    if(auth == null){
      throw new APIException(ErrorCode.UNAUTHORIZED_FAIL);
    }
  }
}

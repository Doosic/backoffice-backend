package com.sideproject.aspects;

import com.sideproject.annotation.BeforeRequiresAuthorization;
import com.sideproject.common.BaseController;
import com.sideproject.domain.entity.RoleAuthEntity;
import com.sideproject.domain.enums.ErrorCode;
import com.sideproject.domain.repository.RoleAuthRepository;
import com.sideproject.exception.APIException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 권한 확인 관련 AOP 구성
 *
 * @author : doosic
 * @fileName : AuthAspect
 * */
@Aspect
@Component
public class AuthAspect extends BaseController{

  private final RoleAuthRepository roleAuthRepository;

  @Autowired
  public AuthAspect(RoleAuthRepository roleAuthRepository){
    this.roleAuthRepository = roleAuthRepository;
  }

  @Before("@annotation(requiresAuthorization)")
  public void checkAuthorization(BeforeRequiresAuthorization requiresAuthorization) {
    String authorization = requiresAuthorization.value();
    RoleAuthEntity auth = roleAuthRepository.findByAuthNameAndRoleId(this.getSessionInfo().getAuthId(), authorization);

    if(auth == null){
      throw new APIException(ErrorCode.UNAUTHORIZED_FAIL);
    }
  }
}

package com.sideproject.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.domain.dto.admin.AdminInfo;
import com.sideproject.exception.AccountException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import static com.sideproject.domain.enums.ErrorCode.UNAUTHORIZED_FAIL;

public abstract class BaseController {

  protected AdminInfo getSessionInfo() throws AccountException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication.getPrincipal().equals("anonymousUser")){
      throw new AccountException(UNAUTHORIZED_FAIL);
    }

    AdminInfo adminInfo = (AdminInfo) authentication.getDetails();
    return adminInfo;
  }

  protected byte[] compressData(String data) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
        gzipOutputStream.write(data.getBytes());
      }
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  protected String convertObjectToJson(Object object) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}

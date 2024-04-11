package com.sideproject.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "backoffice")
@Data
public class CProperties {

  private Jwt jwt;

  @Data
  public static class Jwt {
    private String accessHeader;
    private String refreshHeader;
    private Integer accessTimeoutMin;
    private Integer refreshTimeoutMin;
    private String secret;
  }
}


package com.sideproject.domain.dto.pagenation;

import lombok.Data;

@Data
public class PagenationTotal {

  private final static Long DEFAULT_PAGE_TOTAL_COUNT = 0L;

  private Long itemTotalCnt = DEFAULT_PAGE_TOTAL_COUNT;
}

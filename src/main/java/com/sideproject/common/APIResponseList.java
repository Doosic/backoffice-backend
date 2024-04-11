package com.sideproject.common;

import com.sideproject.domain.dto.pagenation.PagenationTotal;
import lombok.Data;

import java.util.List;

@Data
public class APIResponseList<T> extends PagenationTotal {

  private List<T> itemList;
}

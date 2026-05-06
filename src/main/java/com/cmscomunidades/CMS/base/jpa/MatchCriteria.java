package com.cmscomunidades.CMS.base.jpa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchCriteria {
  private Object value;
  private MatchMode matchMode;
}

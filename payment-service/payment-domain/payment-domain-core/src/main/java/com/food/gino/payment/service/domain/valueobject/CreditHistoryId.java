package com.food.gino.payment.service.domain.valueobject;

import com.gino.food.domain.valueobject.BaseId;
import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {

  public CreditHistoryId(UUID value) {
    super(value);
  }
}

package com.food.gino.payment.service.domain.valueobject;

import com.gino.food.domain.valueobject.BaseId;
import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {

  public CreditEntryId(UUID value) {
    super(value);
  }
}

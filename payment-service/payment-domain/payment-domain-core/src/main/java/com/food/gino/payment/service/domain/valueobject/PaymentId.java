package com.food.gino.payment.service.domain.valueobject;

import com.gino.food.domain.valueobject.BaseId;
import java.util.UUID;

public class PaymentId extends BaseId<UUID> {

  public PaymentId(UUID value) {
    super(value);
  }
}

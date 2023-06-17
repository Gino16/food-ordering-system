package com.food.gino.payment.service.domain.event;

import com.food.gino.payment.service.domain.entity.Payment;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PaymentCancelled extends PaymentEvent {

  public PaymentCancelled(Payment payment,
      ZonedDateTime createdAt) {
    super(payment, createdAt, Collections.emptyList());
  }
}

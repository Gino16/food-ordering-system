package com.food.gino.payment.service.domain.event;

import com.food.gino.payment.service.domain.entity.Payment;
import java.time.ZonedDateTime;
import java.util.List;

public class PaymentCompletedEvent extends PaymentEvent {

  public PaymentCompletedEvent(Payment payment,
      ZonedDateTime createdAt, List<String> failureMessages) {
    super(payment, createdAt, failureMessages);
  }
}

package com.food.gino.payment.service.domain.event;

import com.food.gino.payment.service.domain.entity.Payment;
import com.gino.food.domain.event.DomainEvent;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

public abstract class PaymentEvent implements DomainEvent<Payment> {
  private final Payment payment;
  private final ZonedDateTime createdAt;
  private final List<String> failureMessages;

  public PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
    this.payment = payment;
    this.createdAt = createdAt;
    this.failureMessages = failureMessages;
  }

  public Payment getPayment() {
    return payment;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public List<String> getFailureMessages() {
    return failureMessages;
  }
}

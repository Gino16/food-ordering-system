package com.food.gino.payment.service.domain.event;

import com.food.gino.payment.service.domain.entity.Payment;
import com.gino.food.domain.event.publisher.DomainEventPublisher;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PaymentCompletedEvent extends PaymentEvent {

  private final DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher;

  public PaymentCompletedEvent(Payment payment,
      ZonedDateTime createdAt,
      DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher) {
    super(payment, createdAt, Collections.emptyList());
    this.paymentCompletedEventDomainEventPublisher = paymentCompletedEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    paymentCompletedEventDomainEventPublisher.publish(this);
  }
}

package com.gino.food.order.service.domain;

import com.gino.food.order.service.domain.event.OrderCreatedEvent;
import com.gino.food.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCreatedEventApplicationListener {
  private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

  @TransactionalEventListener
  void process(OrderCreatedEvent orderCreatedEvent) {
    orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
  }
}

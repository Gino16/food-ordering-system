package com.gino.food.order.service.domain;

import com.gino.food.domain.event.EmptyEvent;
import com.gino.food.order.service.domain.dto.message.PaymentResponse;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.event.OrderPaidEvent;
import com.gino.food.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.gino.food.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

  private final OrderDomainService orderDomainService;
  private final OrderSagaHelper orderSagaHelper;
  private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

  @Override
  @Transactional
  public OrderPaidEvent process(PaymentResponse paymentResponse) {
    log.info("Completing payment for order {}", paymentResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
    OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order,
        orderPaidRestaurantRequestMessagePublisher);
    orderSagaHelper.saveOrder(order);
    log.info("Order with id {} paid", order.getId().getValue());
    return orderPaidEvent;
  }

  @Override
  @Transactional
  public EmptyEvent rollback(PaymentResponse paymentResponse) {
    log.info("Rollback payment for order {}", paymentResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
    orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
    orderSagaHelper.saveOrder(order);
    log.info("Order with id {} cancelled", order.getId().getValue());
    return EmptyEvent.INSTANCE;
  }

}

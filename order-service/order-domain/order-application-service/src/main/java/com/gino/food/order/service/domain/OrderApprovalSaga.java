package com.gino.food.order.service.domain;

import com.gino.food.domain.event.EmptyEvent;
import com.gino.food.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.event.OrderCancelledEvent;
import com.gino.food.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.gino.food.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalSaga implements
    SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

  private final OrderDomainService orderDomainService;
  private final OrderSagaHelper orderSagaHelper;
  private final OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;

  @Override
  @Transactional
  public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
    log.info("Approving order {}", restaurantApprovalResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
    orderDomainService.approveOrder(order);
    orderSagaHelper.saveOrder(order);
    log.info("Order with id {} approved", order.getId().getValue());
    return EmptyEvent.INSTANCE;
  }

  @Override
  public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
    log.info("Rollback approval for order {}", restaurantApprovalResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
    OrderCancelledEvent orderCancelledEvent = orderDomainService.cancelOrderPayment(order,
        restaurantApprovalResponse.getFailureMessages(),
        orderCancelledPaymentRequestMessagePublisher);
    orderSagaHelper.saveOrder(order);
    log.info("Order with id {} payment cancelled", order.getId().getValue());
    return orderCancelledEvent;
  }
}

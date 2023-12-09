package com.food.ordering.system.order.service.domain;

import static com.food.ordering.system.domain.DomainConstants.UTC;

import com.food.ordering.system.domain.event.EmptyEvent;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import com.food.ordering.system.saga.SagaStep;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

  private final OrderDomainService orderDomainService;
  private final OrderSagaHelper orderSagaHelper;
  private final PaymentOutboxHelper paymentOutboxHelper;
  private final ApprovalOutboxHelper approvalOutboxHelper;
  private final OrderDataMapper orderDataMapper;

  @Override
  @Transactional
  public void process(PaymentResponse paymentResponse) {

    var orderPaymentOutboxMessageResponse = paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
        UUID.fromString(paymentResponse.getSagaId()),
        SagaStatus.STARTED);

    if (orderPaymentOutboxMessageResponse.isEmpty()) {
      log.error("An outbox message with saga id: {} is already processed!",
          paymentResponse.getSagaId());
      return;
    }

    OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();

    log.info("Completing payment for order with id: {}", paymentResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
    OrderPaidEvent domainEvent = orderDomainService.payOrder(order);
    orderSagaHelper.saveOrder(order);

    SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(
        domainEvent.getOrder().getOrderStatus());

    paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage,
        domainEvent.getOrder().getOrderStatus(), sagaStatus));

    approvalOutboxHelper.saveApprovalOutboxMessage(
        orderDataMapper.orderPaidEventToOrderApprovalEventPayload(domainEvent),
        domainEvent.getOrder().getOrderStatus(),
        sagaStatus,
        OutboxStatus.STARTED,
        UUID.fromString(paymentResponse.getSagaId()));

    log.info("Order with id: {} is paid", order.getId().getValue());
  }


  @Override
  @Transactional
  public void rollback(PaymentResponse paymentResponse) {
    log.info("Cancelling payment for order with id: {}", paymentResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
    orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
    orderSagaHelper.saveOrder(order);
    log.info("Order with id: {} is cancelled", order.getId().getValue());
  }

  private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(
      OrderPaymentOutboxMessage orderPaymentOutboxMessage, OrderStatus orderStatus,
      SagaStatus sagaStatus) {
    orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC
    )));
    orderPaymentOutboxMessage.setOrderStatus(orderStatus);
    orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
    return orderPaymentOutboxMessage;
  }
}

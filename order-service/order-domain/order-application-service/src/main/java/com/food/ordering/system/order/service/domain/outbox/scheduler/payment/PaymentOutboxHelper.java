package com.food.ordering.system.order.service.domain.outbox.scheduler.payment;

import static com.food.ordering.system.outbox.order.SagaConstants.ORDER_SAGA_NAME;

import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxHelper {

  private final PaymentOutboxRepository paymentOutboxRepository;

  @Transactional(readOnly = true)
  public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
      OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
    return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,
        outboxStatus, sagaStatuses);
  }

  @Transactional(readOnly = true)
  public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(
      UUID sagaId, SagaStatus... sagaStatuses) {
    return paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId,
        sagaStatuses);
  }

  @Transactional
  public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
    var response = paymentOutboxRepository.save(orderPaymentOutboxMessage);
    if (response == null) {
      log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}",
          orderPaymentOutboxMessage.getId());
      throw new OrderDomainException("Could not save OrderPaymentOutboxMessage with outbox id:" +
          orderPaymentOutboxMessage.getId());
    }
    log.info("OrderPaymentOutboxMessage saved with outbox id: {}",
        orderPaymentOutboxMessage.getId());
  }

  public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
      SagaStatus... sagaStatuses) {
    paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,
        outboxStatus, sagaStatuses);
  }
}

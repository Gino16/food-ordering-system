package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import domain.PaymentDomainService;
import domain.entity.CreditEntry;
import domain.entity.CreditHistory;
import domain.entity.Payment;
import domain.event.PaymentEvent;
import domain.exception.PaymentNotFoundException;
import java.util.ArrayList;
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
public class PaymentRequestHelper {

  private final PaymentDomainService paymentDomainService;
  private final PaymentDataMapper paymentDataMapper;
  private final PaymentRepository paymentRepository;
  private final CreditEntryRepository creditEntryRepository;
  private final CreditHistoryRepository creditHistoryRepository;
  private final OrderOutboxHelper orderOutboxHelper;
  private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

  @Transactional
  public void persistPayment(PaymentRequest paymentRequest) {

    if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
      log.info("An outbox message with saga id: {} is al ready saved to database!",
          paymentRequest.getSagaId());
      return;
    }

    log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
    Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
    buildPaymentEvent(payment, PaymentStatus.COMPLETED, paymentRequest.getSagaId());
  }


  @Transactional
  public void persistCancelPayment(PaymentRequest paymentRequest) {
    if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
      log.info("An outbox message with saga id: {} is al ready saved to database!",
          paymentRequest.getSagaId());
      return;
    }

    log.info("Received payment cancel event for order id: {}", paymentRequest.getOrderId());
    Optional<Payment> paymentResponse =
        paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
    if (paymentResponse.isEmpty()) {
      log.error("Payment with order id: {} could not be found", paymentRequest.getOrderId());
      throw new PaymentNotFoundException(
          "Payment with order id:" + paymentRequest.getOrderId() + " could not be found");
    }
    buildPaymentEvent(paymentResponse.get(), PaymentStatus.CANCELLED, paymentRequest.getSagaId());
  }


  private CreditEntry getCreditEntry(CustomerId customerId) {
    Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
    if (creditEntry.isEmpty()) {
      log.error("Could not find credit entry for customer id: {}", customerId.getValue());
      throw new PaymentApplicationServiceException("Could not find credit entry for customer id: "
          + customerId.getValue());
    }
    return creditEntry.get();
  }


  private List<CreditHistory> getCreditHistory(CustomerId customerId) {
    Optional<List<CreditHistory>> creditHistories =
        creditHistoryRepository.findByCustomerId(customerId);
    if (creditHistories.isEmpty()) {
      log.error("Could not find credit history for customer id: {}", customerId.getValue());
      throw new PaymentApplicationServiceException("Could not find credit history for customer id: "
          + customerId.getValue());
    }
    return creditHistories.get();
  }

  private void persistDbObject(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages) {
    paymentRepository.save(payment);
    if (failureMessages.isEmpty()) {
      creditEntryRepository.save(creditEntry);
      creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
    }
  }

  private void buildPaymentEvent(Payment payment,
      PaymentStatus paymentStatus, String sagaId) {
    CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
    List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
    List<String> failureMessages = new ArrayList<>();
    PaymentEvent paymentEvent;

    if (PaymentStatus.COMPLETED == paymentStatus) {
      paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry,
          creditHistories,
          failureMessages);
    } else {
      paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry,
          creditHistories,
          failureMessages);
    }
    persistDbObject(payment, creditEntry, creditHistories, failureMessages);
    orderOutboxHelper.saveOrderOutboxMessage(
        paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
        paymentEvent.getPayment().getPaymentStatus(), OutboxStatus.STARTED,
        UUID.fromString(sagaId));
  }

  private final boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest,
      PaymentStatus paymentStatus) {
    var orderOutboxMessage = orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(
        UUID.fromString(paymentRequest.getSagaId()), paymentStatus);

    if (orderOutboxMessage.isPresent()) {
      paymentResponseMessagePublisher.publish(orderOutboxMessage.get(),
          orderOutboxHelper::updateOutboxMessage);
      return true;
    }
    return false;
  }

}

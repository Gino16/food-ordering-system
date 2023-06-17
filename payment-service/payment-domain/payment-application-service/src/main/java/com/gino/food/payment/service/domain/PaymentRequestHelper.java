package com.gino.food.payment.service.domain;

import com.food.gino.payment.service.domain.PaymentDomainService;
import com.food.gino.payment.service.domain.entity.CreditEntry;
import com.food.gino.payment.service.domain.entity.CreditHistory;
import com.food.gino.payment.service.domain.entity.Payment;
import com.food.gino.payment.service.domain.event.PaymentEvent;
import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.payment.service.domain.dto.PaymentRequest;
import com.gino.food.payment.service.domain.exception.PaymentApplicationServiceException;
import com.gino.food.payment.service.domain.mapper.PaymentDataMapper;
import com.gino.food.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.gino.food.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.gino.food.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.gino.food.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.gino.food.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.gino.food.payment.service.domain.ports.output.repository.PaymentRepository;
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
  private final PaymentCompletedMessagePublisher paymentCompletedEventDomainEventPublisher;
  private final PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher;
  private final PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher;

  @Transactional
  public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
    log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
    Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
    CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
    List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
    List<String> failureMessages = new ArrayList<>();
    PaymentEvent paymentEvent =
        paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories,
            failureMessages, paymentCompletedEventDomainEventPublisher,
            paymentFailedEventDomainEventPublisher);
    persistDbObject(payment, creditEntry, creditHistories, failureMessages);
    return paymentEvent;
  }

  @Transactional
  public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
    log.info("Received payment cancel event for order id: {}", paymentRequest.getOrderId());
    Optional<Payment> paymentResponse = paymentRepository.findByOrderId(
        UUID.fromString(paymentRequest.getOrderId()));
    if (paymentResponse.isEmpty()) {
      log.error("Payment not found for order id: {}", paymentRequest.getOrderId());
      throw new PaymentApplicationServiceException(
          "Payment not found for order id: " + paymentRequest.getOrderId());
    }
    Payment payment = paymentResponse.get();
    CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
    List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
    List<String> failureMessages = new ArrayList<>();
    PaymentEvent paymentEvent =
        paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories,
            failureMessages, paymentCancelledEventDomainEventPublisher,
            paymentFailedEventDomainEventPublisher);
    persistDbObject(payment, creditEntry, creditHistories, failureMessages);
    return paymentEvent;
  }

  private void persistDbObject(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages) {
    paymentRepository.save(payment);
    if (failureMessages.isEmpty()) {
      creditEntryRepository.save(creditEntry);
      creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
    }
  }


  private CreditEntry getCreditEntry(CustomerId customerId) {
    Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
    if (creditEntry.isEmpty()) {
      log.error("Credit entry not found for customer id: {}", customerId);
      throw new PaymentApplicationServiceException(
          "Credit entry not found for customer id: " + customerId);
    }
    return creditEntry.get();
  }

  private List<CreditHistory> getCreditHistory(CustomerId customerId) {
    Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(
        customerId);
    if (creditHistories.isEmpty()) {
      log.error("Credit history not found for customer id: {}", customerId);
      throw new PaymentApplicationServiceException(
          "Credit history not found for customer id: " + customerId);
    }
    return creditHistories.get();
  }
}

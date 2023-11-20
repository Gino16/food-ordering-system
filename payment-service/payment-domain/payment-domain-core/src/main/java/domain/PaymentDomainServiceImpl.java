package domain;


import com.food.ordering.system.domain.DomainConstants;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import domain.entity.CreditEntry;
import domain.entity.CreditHistory;
import domain.entity.Payment;
import domain.event.PaymentCancelledEvent;
import domain.event.PaymentCompletedEvent;
import domain.event.PaymentEvent;
import domain.event.PaymentFailedEvent;
import domain.valueobject.CreditHistoryId;
import domain.valueobject.TransactionType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {


  @Override
  public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages,
      DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {
    payment.validatePayment(failureMessages);
    payment.initializePayment();
    validateCreditEntry(payment, creditEntry, failureMessages);
    subtractCreditEntry(payment, creditEntry);
    updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
    validateCreditHistory(creditEntry, creditHistories, failureMessages);

    if (failureMessages.isEmpty()) {
      log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.COMPLETED);
      return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)),
          paymentCompletedEventDomainEventPublisher);
    } else {
      log.info("Payment initiation is failed for order id: {}", payment.getOrderId());
      payment.updateStatus(PaymentStatus.FAILED);
      return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)),
          failureMessages, paymentFailedEventDomainEventPublisher);
    }
  }


  @Override
  public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages,
      DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {
    payment.validatePayment(failureMessages);
    addCreditEntry(payment, creditEntry);
    updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);

    if (failureMessages.isEmpty()) {
      log.info("Payment is cancelled for order id: {}", payment.getOrderId());
      payment.updateStatus(PaymentStatus.CANCELLED);
      return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)),
          paymentCancelledEventDomainEventPublisher);
    } else {
      log.info("Payment cancellation is failed for order id: {}", payment.getOrderId());
      return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)),
          failureMessages, paymentFailedEventDomainEventPublisher);
    }

  }


  private void validateCreditEntry(Payment payment, CreditEntry creditEntry,
      List<String> failureMessages) {
    if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
      log.error("Customer with id: {} doesn't have enough credit for payment!",
          payment.getCustomerId().getValue());
      failureMessages.add("Customer with id " + payment.getCustomerId().getValue() + "doesn't "
          + "have enough credit for payment!");
    }
  }


  private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.subtractCreditAmount(payment.getPrice());
  }

  private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories,
      TransactionType transactionType) {
    creditHistories.add(CreditHistory.builder()
        .id(new CreditHistoryId(UUID.randomUUID()))
        .customerId(payment.getCustomerId())
        .amount(payment.getPrice())
        .transactionType(transactionType)
        .build());
  }

  private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories,
      List<String> failureMessages) {
    Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);

    Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

    if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
      log.error("Customer with id: {} doesn't have enough credit according to credit history",
          creditEntry.getCustomerId().getValue());
      failureMessages.add("Customer with id:" + creditEntry.getCustomerId().getValue() +
          " doesn't have enough credit according to credit history");
    }

    if (creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
      log.error("Credit history total is not equal to current credit for customer id: {}",
          creditEntry.getCustomerId().getValue());
      failureMessages.add("Credit history total is not equal to current credit for customer id: " +
          creditEntry.getCustomerId().getValue());
    }
  }

  private static Money getTotalHistoryAmount(List<CreditHistory> creditHistories,
      TransactionType transactionType) {
    return creditHistories.stream()
        .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
        .map(CreditHistory::getAmount)
        .reduce(Money.ZERO, Money::add);
  }


  private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.addCreditAmount(payment.getPrice());
  }
}

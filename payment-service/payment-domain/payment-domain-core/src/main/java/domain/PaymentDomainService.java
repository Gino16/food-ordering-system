package domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import domain.entity.CreditEntry;
import domain.entity.CreditHistory;
import domain.entity.Payment;
import domain.event.PaymentCancelledEvent;
import domain.event.PaymentCompletedEvent;
import domain.event.PaymentEvent;
import domain.event.PaymentFailedEvent;
import java.util.List;

public interface PaymentDomainService {

  PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages,
      DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);

  PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages,
      DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);
}

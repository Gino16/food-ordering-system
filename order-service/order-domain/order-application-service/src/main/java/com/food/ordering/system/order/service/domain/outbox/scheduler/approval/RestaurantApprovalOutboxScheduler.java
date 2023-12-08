package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler {

  private final ApprovalOutboxHelper approvalOutboxHelper;
  private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

  @Override
  @Transactional
  @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
      initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
  public void processOutboxMessage() {
    var outboxMessageResponse =
        approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus.STARTED, SagaStatus.STARTED, SagaStatus.PROCESSING);

    if (outboxMessageResponse.isPresent() && !outboxMessageResponse.get().isEmpty()) {
      var outboxMessages = outboxMessageResponse.get();
      log.info("Received {} OrderApprovalOutboxMessage with ids: {}, sending to message bus",
          outboxMessages.size(),
          outboxMessages.stream().map(outboxMessage -> outboxMessage.getId().toString()).collect(
              Collectors.joining(",")));

      outboxMessages.forEach(
          outboxMessage -> restaurantApprovalRequestMessagePublisher.publish(outboxMessage,
              this::updateOutboxStatus));
      log.info("{} OrderApprovalOutboxMessage sent to message bus!", outboxMessages.size());
    }
  }

  public void updateOutboxStatus(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
      OutboxStatus outboxStatus) {
    approvalOutboxHelper.save(orderApprovalOutboxMessage);
    log.info("OrderApprovalOutboxMessage is updated with outbox status: {}", outboxStatus.name());
  }
}

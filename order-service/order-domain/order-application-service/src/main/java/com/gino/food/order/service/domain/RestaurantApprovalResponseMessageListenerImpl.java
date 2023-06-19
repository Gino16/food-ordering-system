package com.gino.food.order.service.domain;

import static com.gino.food.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

import com.gino.food.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.gino.food.order.service.domain.event.OrderCancelledEvent;
import com.gino.food.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class RestaurantApprovalResponseMessageListenerImpl implements
    RestaurantApprovalResponseMessageListener {

  private final OrderApprovalSaga orderApprovalSaga;

  @Override
  public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {
    orderApprovalSaga.process(restaurantApprovalResponse);
    log.info("Order with id {} approved", restaurantApprovalResponse.getOrderId());
  }

  @Override
  public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
    OrderCancelledEvent orderCancelledEvent = orderApprovalSaga.rollback(
        restaurantApprovalResponse);
    log.info("Order with id {} rejected with failure messages {}",
        restaurantApprovalResponse.getOrderId(),
        String.join(FAILURE_MESSAGE_DELIMITER, restaurantApprovalResponse.getFailureMessages()));
    orderCancelledEvent.fire();
  }
}

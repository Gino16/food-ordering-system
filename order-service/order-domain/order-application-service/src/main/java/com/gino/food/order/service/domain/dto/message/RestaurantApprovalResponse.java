package com.gino.food.order.service.domain.dto.message;

import com.gino.food.domain.valueobject.OrderApprovalStatus;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponse {

  private String id;
  private String sagaId;
  private String orderId;
  private String restaurantId;
  private Instant createdAt;
  private OrderApprovalStatus orderApprovalStatus;
  private List<String> failureMessages;

}

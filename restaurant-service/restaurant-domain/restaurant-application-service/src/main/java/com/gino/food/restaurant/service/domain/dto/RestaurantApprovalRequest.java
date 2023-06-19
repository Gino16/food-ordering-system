package com.gino.food.restaurant.service.domain.dto;

import com.gino.food.domain.valueobject.RestaurantOrderStatus;
import com.gino.food.restaurant.service.domain.entity.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RestaurantApprovalRequest {

  private String id;
  private String sagaId;
  private String restaurantId;
  private String orderId;
  private RestaurantOrderStatus restaurantOrderStatus;
  private List<Product> products;
  private BigDecimal price;
  private Instant createdAt;
}

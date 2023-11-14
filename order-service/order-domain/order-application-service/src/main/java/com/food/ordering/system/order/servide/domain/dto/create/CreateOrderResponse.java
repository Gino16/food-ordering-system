package com.food.ordering.system.order.servide.domain.dto.create;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

  @NotNull
  private final UUID orderTrackingId;
  @NotNull
  private final OrderStatus orderStatus;
  @NotNull
  private final String message;
}

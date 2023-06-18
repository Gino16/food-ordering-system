package com.gino.food.restaurant.service.domain.entity;

import com.gino.food.domain.entity.AggregateRoot;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.OrderApprovalStatus;
import com.gino.food.domain.valueobject.OrderStatus;
import com.gino.food.domain.valueobject.RestaurantId;
import com.gino.food.restaurant.service.domain.valueobject.OrderApprovalId;
import java.util.List;
import java.util.UUID;

public class Restaurant extends AggregateRoot<RestaurantId> {

  private final OrderDetail orderDetail;
  private OrderApproval orderApproval;
  private boolean active;

  private Restaurant(Builder builder) {
    setId(builder.restaurantId);
    orderApproval = builder.orderApproval;
    active = builder.active;
    orderDetail = builder.orderDetail;
  }

  public void validateOrder(List<String> failureMessages) {
    if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
      failureMessages.add("Payment is not completed for order " + orderDetail.getId());
    }
    Money totalAmount = orderDetail.getProducts().stream().map(product -> {
      if (!product.isAvailable()) {
        failureMessages.add("Product " + product.getName() + " is not available");
      }
      return product.getPrice().multiply(product.getQuantity());
    }).reduce(Money.ZERO, Money::add);

    if (!totalAmount.equals(orderDetail.getTotalAmount())) {
      failureMessages.add("Total amount is not correct");
    }
  }

  public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
    this.orderApproval = OrderApproval.Builder.builder()
        .id(new OrderApprovalId(UUID.randomUUID()))
        .restaurantId(this.getId())
        .orderId(this.getOrderDetail().getId())
        .approvalStatus(orderApprovalStatus)
        .build();
  }

  public OrderApproval getOrderApproval() {
    return orderApproval;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public OrderDetail getOrderDetail() {
    return orderDetail;
  }

  public static final class Builder {

    private RestaurantId restaurantId;
    private OrderApproval orderApproval;
    private boolean active;
    private OrderDetail orderDetail;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder restaurantId(RestaurantId val) {
      restaurantId = val;
      return this;
    }

    public Builder orderApproval(OrderApproval val) {
      orderApproval = val;
      return this;
    }

    public Builder active(boolean val) {
      active = val;
      return this;
    }

    public Builder orderDetail(OrderDetail val) {
      orderDetail = val;
      return this;
    }

    public Restaurant build() {
      return new Restaurant(this);
    }
  }
}

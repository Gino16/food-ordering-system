package com.gino.food.restaurant.service.domain.ports.input.message.listener;

import com.gino.food.restaurant.service.domain.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {

  void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);

}

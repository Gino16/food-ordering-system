package com.gino.food.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {

}

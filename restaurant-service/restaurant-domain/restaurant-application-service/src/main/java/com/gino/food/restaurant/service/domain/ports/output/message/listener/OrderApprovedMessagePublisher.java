package com.gino.food.restaurant.service.domain.ports.output.message.listener;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.restaurant.service.domain.event.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {

}

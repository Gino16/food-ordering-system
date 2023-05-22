package com.gino.food.domain.event.publisher;

import com.gino.food.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
  void publish(T domainEvent);
}

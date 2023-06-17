package com.gino.food.domain.event;

public interface DomainEvent<T> {
  void fire();
}

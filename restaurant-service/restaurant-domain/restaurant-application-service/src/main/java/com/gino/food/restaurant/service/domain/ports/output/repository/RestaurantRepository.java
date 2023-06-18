package com.gino.food.restaurant.service.domain.ports.output.repository;

import com.gino.food.restaurant.service.domain.entity.Restaurant;
import java.util.Optional;

public interface RestaurantRepository {
  Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}

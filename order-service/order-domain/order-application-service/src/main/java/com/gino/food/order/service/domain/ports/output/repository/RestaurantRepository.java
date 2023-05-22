package com.gino.food.order.service.domain.ports.output.repository;

import com.gino.food.order.service.domain.entity.Restaurant;
import java.util.Optional;

public interface RestaurantRepository {

  Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}

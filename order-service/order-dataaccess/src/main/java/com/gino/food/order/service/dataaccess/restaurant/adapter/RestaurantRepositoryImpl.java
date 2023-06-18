package com.gino.food.order.service.dataaccess.restaurant.adapter;

import com.gino.food.dataaccess.restaurant.entity.RestaurantEntity;
import com.gino.food.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.gino.food.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.gino.food.order.service.domain.entity.Restaurant;
import com.gino.food.order.service.domain.ports.output.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

  private final RestaurantJpaRepository restaurantJpaRepository;
  private final RestaurantDataAccessMapper restaurantDataAccessMapper;

  @Override
  public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
    List<UUID> restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(
        restaurant);
    Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(
        restaurant.getId().getValue(), restaurantProducts);

    return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
  }
}

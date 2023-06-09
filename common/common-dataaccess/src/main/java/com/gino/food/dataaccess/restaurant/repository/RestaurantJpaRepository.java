package com.gino.food.dataaccess.restaurant.repository;

import com.gino.food.dataaccess.restaurant.entity.RestaurantEntity;
import com.gino.food.dataaccess.restaurant.entity.RestaurantEntityId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantJpaRepository extends
    JpaRepository<RestaurantEntity, RestaurantEntityId> {

  Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId,
      List<UUID> productIds);
}

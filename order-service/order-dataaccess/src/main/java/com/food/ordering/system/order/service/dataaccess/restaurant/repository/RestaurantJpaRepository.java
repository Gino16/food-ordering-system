package com.food.ordering.system.order.service.dataaccess.restaurant.repository;

import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntityId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RestaurantJpaRepository extends CrudRepository<RestaurantEntity, RestaurantEntityId> {
  Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId,
      List<UUID> productIds);
}

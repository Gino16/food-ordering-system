package com.gino.food.order.service.dataaccess.customer.repository;

import com.gino.food.order.service.dataaccess.customer.entity.CustomerEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}

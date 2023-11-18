package com.food.ordering.system.order.service.dataaccess.customer.repository;


import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface CustomerJpaRepository extends Repository<CustomerEntity, UUID> {
  Optional<CustomerEntity> findById(UUID id);
}

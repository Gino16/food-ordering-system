package com.gino.food.order.service.dataaccess.customer.adapter;

import com.gino.food.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.gino.food.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.gino.food.order.service.domain.entity.Customer;
import com.gino.food.order.service.domain.ports.output.repository.CustomerRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomerRepositoryImpl implements CustomerRepository {

  private final CustomerJpaRepository customerJpaRepository;
  private final CustomerDataAccessMapper customerDataAccessMapper;

  @Override
  public Optional<Customer> findCustomer(UUID customerId) {
    return customerJpaRepository.findById(customerId)
        .map(customerDataAccessMapper::customerEntityToCustomer);
  }
}

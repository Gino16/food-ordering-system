package com.food.ordering.system.customer.service.dataaccess.customer.adapter;

import com.food.ordering.system.customer.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.food.ordering.system.customer.service.dataaccess.customer.repository.CustomerJpaRepository;
import entity.Customer;
import org.springframework.stereotype.Component;
import ports.output.repository.CustomerRepository;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

  private final CustomerJpaRepository customerJpaRepository;

  private final CustomerDataAccessMapper customerDataAccessMapper;

  public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository,
      CustomerDataAccessMapper customerDataAccessMapper) {
    this.customerJpaRepository = customerJpaRepository;
    this.customerDataAccessMapper = customerDataAccessMapper;
  }

  @Override
  public Customer createCustomer(Customer customer) {
    return customerDataAccessMapper.customerEntityToCustomer(
        customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
  }
}

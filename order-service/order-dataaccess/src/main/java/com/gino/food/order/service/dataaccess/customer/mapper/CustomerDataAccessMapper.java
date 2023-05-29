package com.gino.food.order.service.dataaccess.customer.mapper;

import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.order.service.dataaccess.customer.entity.CustomerEntity;
import com.gino.food.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

  public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
    return new Customer(new CustomerId(customerEntity.getId()));
  }
}

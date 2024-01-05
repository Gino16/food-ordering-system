package ports.output.repository;


import entity.Customer;

public interface CustomerRepository {

  Customer createCustomer(Customer customer);
}

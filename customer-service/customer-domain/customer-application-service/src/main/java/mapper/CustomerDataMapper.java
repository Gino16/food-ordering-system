package mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import create.CreateCustomerCommand;
import create.CreateCustomerResponse;
import entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {

  public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
    return new Customer(new CustomerId(createCustomerCommand.getCustomerId()),
        createCustomerCommand.getUsername(),
        createCustomerCommand.getFirstName(),
        createCustomerCommand.getLastName());
  }

  public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer,
      String message) {
    return new CreateCustomerResponse(customer.getId().getValue(), message);
  }
}

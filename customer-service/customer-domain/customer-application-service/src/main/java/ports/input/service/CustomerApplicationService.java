package ports.input.service;

import create.CreateCustomerCommand;
import create.CreateCustomerResponse;
import javax.validation.Valid;

public interface CustomerApplicationService {

  CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);

}

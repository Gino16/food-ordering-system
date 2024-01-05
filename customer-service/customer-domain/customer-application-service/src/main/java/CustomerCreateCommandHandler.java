import create.CreateCustomerCommand;
import entity.Customer;
import event.CustomerCreatedEvent;
import exception.CustomerDomainException;
import lombok.extern.slf4j.Slf4j;
import mapper.CustomerDataMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ports.output.repository.CustomerRepository;

@Slf4j
@Component
class CustomerCreateCommandHandler {

  private final CustomerDomainService customerDomainService;

  private final CustomerRepository customerRepository;

  private final CustomerDataMapper customerDataMapper;

  public CustomerCreateCommandHandler(CustomerDomainService customerDomainService,
      CustomerRepository customerRepository,
      CustomerDataMapper customerDataMapper) {
    this.customerDomainService = customerDomainService;
    this.customerRepository = customerRepository;
    this.customerDataMapper = customerDataMapper;
  }

  @Transactional
  public CustomerCreatedEvent createCustomer(CreateCustomerCommand createCustomerCommand) {
    Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
    CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(
        customer);
    Customer savedCustomer = customerRepository.createCustomer(customer);
    if (savedCustomer == null) {
      log.error("Could not save customer with id: {}", createCustomerCommand.getCustomerId());
      throw new CustomerDomainException("Could not save customer with id " +
          createCustomerCommand.getCustomerId());
    }
    log.info("Returning CustomerCreatedEvent for customer id: {}",
        createCustomerCommand.getCustomerId());
    return customerCreatedEvent;
  }
}

import create.CreateCustomerCommand;
import create.CreateCustomerResponse;
import event.CustomerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import mapper.CustomerDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ports.input.service.CustomerApplicationService;
import ports.output.message.publisher.CustomerMessagePublisher;

@Slf4j
@Validated
@Service
class CustomerApplicationServiceImpl implements CustomerApplicationService {

  private final CustomerCreateCommandHandler customerCreateCommandHandler;

  private final CustomerDataMapper customerDataMapper;

  private final CustomerMessagePublisher customerMessagePublisher;

  public CustomerApplicationServiceImpl(CustomerCreateCommandHandler customerCreateCommandHandler,
      CustomerDataMapper customerDataMapper,
      CustomerMessagePublisher customerMessagePublisher) {
    this.customerCreateCommandHandler = customerCreateCommandHandler;
    this.customerDataMapper = customerDataMapper;
    this.customerMessagePublisher = customerMessagePublisher;
  }

  @Override
  public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
    CustomerCreatedEvent customerCreatedEvent = customerCreateCommandHandler.createCustomer(
        createCustomerCommand);
    customerMessagePublisher.publish(customerCreatedEvent);
    return customerDataMapper
        .customerToCreateCustomerResponse(customerCreatedEvent.getCustomer(),
            "Customer saved successfully!");
  }
}

import entity.Customer;
import event.CustomerCreatedEvent;

public interface CustomerDomainService {

  CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);

}

package ports.output.message.publisher;


import event.CustomerCreatedEvent;

public interface CustomerMessagePublisher {

  void publish(CustomerCreatedEvent customerCreatedEvent);

}
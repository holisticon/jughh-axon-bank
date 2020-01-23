package de.holisticon.axon.bank.context.customer.domain.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import de.holisticon.axon.bank.context.customer.api.command.RegisterCustomerCommand;
import de.holisticon.axon.bank.context.customer.api.event.CustomerRegisteredEvent;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
@Slf4j
public class CustomerAggregate {

  @AggregateIdentifier
  private String customerId;

  @CommandHandler
  public static CustomerAggregate handle(RegisterCustomerCommand cmd) {
    apply(
      CustomerRegisteredEvent.builder()
        .customerId(Optional.ofNullable(cmd.getCustomerId()).orElse(UUID.randomUUID().toString()))
        .name(cmd.getName())
        .build()
    );
    return new CustomerAggregate();
  }

  @EventSourcingHandler
  void on(CustomerRegisteredEvent evt) {
    this.customerId = evt.getCustomerId();
  }
}

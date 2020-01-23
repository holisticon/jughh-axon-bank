package de.holisticon.axon.bank.context.customer.api.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class RegisterCustomerCommand {

  @TargetAggregateIdentifier
  private String customerId;

  @NonNull
  private String name;

}

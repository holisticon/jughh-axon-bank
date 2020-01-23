package de.holisticon.axon.bank.context.customer.api.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class CustomerRegisteredEvent {

  @NonNull
  private String customerId;

  @NonNull
  private String name;

}

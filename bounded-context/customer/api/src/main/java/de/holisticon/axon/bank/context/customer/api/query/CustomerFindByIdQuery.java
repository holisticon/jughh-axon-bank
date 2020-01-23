package de.holisticon.axon.bank.context.customer.api.query;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class CustomerFindByIdQuery {
  @NonNull
  private String customerId;
}

package de.holisticon.axon.bank.context.account.api.query;

import lombok.Builder;
import lombok.Value;

@Value(staticConstructor = "of")
public class AccountFindByCustomerIdQuery {

  private String customerId;
}

package de.holisticon.axon.bank.context.account.api.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class AccountCreatedEvent {

  @NonNull
  private String accountId;

  @NonNull
  private String customerId;

  private int initialBalance;

  private int maximalBalance;

}

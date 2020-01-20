package de.holisticon.axon.bank.context.account.domain.api.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class BankAccountCreatedEvent {

  @NonNull
  private String accountId;

  @NonNull
  private String customerId;

  @NonNull
  private Integer initialBalance;
}

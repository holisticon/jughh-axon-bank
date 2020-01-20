package de.holisticon.axon.bank.context.account.domain.api.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class MoneyWithdrawnEvent {

  @NonNull
  private String accountId;

  @NonNull
  private Integer amount;
}

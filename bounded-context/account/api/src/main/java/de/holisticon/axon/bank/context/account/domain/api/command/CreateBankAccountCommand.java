package de.holisticon.axon.bank.context.account.domain.api.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class CreateBankAccountCommand {

  @NonNull
  @TargetAggregateIdentifier
  private String accountId;

  @NonNull
  private String customerId;

  @NonNull
  @Builder.Default
  private Integer initialBalance = 0;
}

package de.holisticon.axon.bank.context.account.domain.api.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Revision("1")
@Builder(toBuilder = true)
public class DepositMoneyCommand {

  @NonNull
  @TargetAggregateIdentifier
  private String accountId;

  @NonNull
  private Integer amount;
}

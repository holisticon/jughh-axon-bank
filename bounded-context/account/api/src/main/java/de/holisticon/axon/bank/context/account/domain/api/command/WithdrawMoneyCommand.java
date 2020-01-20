package de.holisticon.axon.bank.context.account.domain.api.command;


import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class WithdrawMoneyCommand {

  @NonNull
  @TargetAggregateIdentifier
  private String accountId;

  @NonNull
  private Integer amount;
}

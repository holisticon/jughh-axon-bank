package de.holisticon.axon.bank.context.account.api.command.moneytransfer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class RollBackMoneyTransferCommand {

  @TargetAggregateIdentifier
  @NonNull
  private String sourceAccountId;

  @NonNull
  private String transactionId;

}

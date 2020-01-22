package de.holisticon.axon.bank.context.account.domain.api.event.moneytransfer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

  @Value
  @Builder
  public class MoneyTransferReceivedEvent {

    @NonNull
    private String targetAccountId;

    @NonNull
    private String sourceAccountId;

    @NonNull
    private String transactionId;

    private int amount;

}

package de.holisticon.axon.bank.context.account.domain.api.event.moneytransfer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class MoneyTransferCompletedEvent {

  @NonNull
  private String sourceAccountId;

  @NonNull
  private String transactionId;

  private int amount;

}

package de.holisticon.axon.bank.context.account.api.event.moneytransfer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class MoneyTransferRolledBackEvent {

  @NonNull
  private String sourceAccountId;

  @NonNull
  private String transactionId;

}

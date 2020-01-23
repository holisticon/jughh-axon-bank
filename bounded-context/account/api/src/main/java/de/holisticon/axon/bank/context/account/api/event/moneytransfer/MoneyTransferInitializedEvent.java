package de.holisticon.axon.bank.context.account.api.event.moneytransfer;

import lombok.Builder;
import lombok.Value;
import org.axonframework.serialization.Revision;

@Value
@Builder
@Revision("1")
public class MoneyTransferInitializedEvent {

  private String sourceAccountId;
  private String targetAccountId;

  private String transactionId;

  private int amount;
}

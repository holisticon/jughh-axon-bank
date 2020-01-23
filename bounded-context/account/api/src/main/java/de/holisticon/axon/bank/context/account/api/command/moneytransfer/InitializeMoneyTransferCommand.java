package de.holisticon.axon.bank.context.account.api.command.moneytransfer;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Builder
@Revision("1")
public class InitializeMoneyTransferCommand {

  @TargetAggregateIdentifier
  private String sourceAccountId;

  private String targetAccountId;

  @Builder.Default
  private String transactionId = UUID.randomUUID().toString();

  private int amount;

}

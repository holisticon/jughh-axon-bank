package de.holisticon.axon.bank.context.account.command;


import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class WithdrawAmountCommand {

  @NonNull
  @TargetAggregateIdentifier
  private String accountId;

  @NonNull
  private BigDecimal amount;
}

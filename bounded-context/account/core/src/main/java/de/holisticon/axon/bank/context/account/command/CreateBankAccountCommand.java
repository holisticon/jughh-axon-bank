package de.holisticon.axon.bank.context.account.command;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateBankAccountCommand {

  @NonNull
  private String accountId;

  @NonNull
  private String customerId;

  @NonNull
  @Builder.Default
  private BigDecimal initialBalance = BigDecimal.ZERO;
}

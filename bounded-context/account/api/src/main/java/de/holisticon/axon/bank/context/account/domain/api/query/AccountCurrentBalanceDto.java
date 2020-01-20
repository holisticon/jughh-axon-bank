package de.holisticon.axon.bank.context.account.domain.api.query;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AccountCurrentBalanceDto {

  private String accountId;

  private int currentBalance;

}

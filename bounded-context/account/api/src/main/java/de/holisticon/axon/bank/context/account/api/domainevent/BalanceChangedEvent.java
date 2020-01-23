package de.holisticon.axon.bank.context.account.api.domainevent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BalanceChangedEvent {

  private String accountId;

  private int newBalance;

}

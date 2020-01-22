package de.holisticon.axon.bank.context.account.domain;

import static de.holisticon.axon.bank.context.account.domain.aggregate.AccountAggregate.Configuration.DEFAULT_INITIAL_BALANCE;
import static de.holisticon.axon.bank.context.account.domain.aggregate.AccountAggregate.Configuration.DEFAULT_MAXIMAL_BALANCE;

import de.holisticon.axon.bank.context.account.domain.aggregate.AccountAggregate;
import de.holisticon.axon.bank.context.account.domain.api.command.CreateAccountCommand;
import de.holisticon.axon.bank.context.account.domain.api.event.AccountCreatedEvent;
import java.util.UUID;

public enum AccountAggregateTestHelper {
  ;

  public static final String ACCOUNT_ID_1 = UUID.randomUUID().toString();
  public static final String CUSTOMER_ID_1 = UUID.randomUUID().toString();

  public static final String ACCOUNT_ID_2 = UUID.randomUUID().toString();
  public static final String CUSTOMER_ID_2 = UUID.randomUUID().toString();

  public static AccountCreatedEvent accountCreatedEvent(String accountId, String customerId) {
    return AccountCreatedEvent.builder()
      .accountId(accountId)
      .customerId(customerId)
      .maximalBalance(DEFAULT_MAXIMAL_BALANCE)
      .initialBalance(DEFAULT_INITIAL_BALANCE)
      .build();
  }

  public static CreateAccountCommand createAccountCommand(String accountId, String customerId) {
    return CreateAccountCommand.builder()
      .accountId(accountId)
      .customerId(customerId)
      .build();
  }

  public static AccountAggregate accountAggregate(String accountId, int currentBalance, int maximalBalance) {
    return AccountAggregate.builder()
      .accountId(accountId)
      .currentBalance(currentBalance)
      .maximalBalance(maximalBalance)
      .build();
  }
}

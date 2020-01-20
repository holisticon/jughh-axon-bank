package de.holisticon.axon.bank.context.account.domain.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import de.holisticon.axon.bank.context.account.domain.api.command.CreateBankAccountCommand;
import de.holisticon.axon.bank.context.account.domain.api.command.DepositMoneyCommand;
import de.holisticon.axon.bank.context.account.domain.api.command.WithdrawMoneyCommand;
import de.holisticon.axon.bank.context.account.domain.api.event.BankAccountCreatedEvent;
import de.holisticon.axon.bank.context.account.domain.api.event.MoneyDepositedEvent;
import de.holisticon.axon.bank.context.account.domain.api.event.MoneyWithdrawnEvent;
import de.holisticon.axon.bank.context.account.domain.api.exception.InsufficientBalanceException;
import java.util.UUID;
import java.util.function.Consumer;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.Test;

class AccountAggregateTest {

  private final AggregateTestFixture<AccountAggregate> fixture = new AggregateTestFixture<>(AccountAggregate.class);

  private static final String ACCOUNT_ID = UUID.randomUUID().toString();
  private static final String CUSTOMER_ID = UUID.randomUUID().toString();

  @Test
  void create_account() {
    fixture
      .givenNoPriorActivity()
      .when(
        CreateBankAccountCommand.builder().accountId(ACCOUNT_ID).customerId(CUSTOMER_ID).build()
      )
      .expectEvents(
        BankAccountCreatedEvent.builder().accountId(ACCOUNT_ID).customerId(CUSTOMER_ID).initialBalance(0).build()
      );
  }

  @Test
  void withdraw_amount() {
    fixture
      .given(
        BankAccountCreatedEvent.builder().accountId(ACCOUNT_ID).customerId(CUSTOMER_ID).initialBalance(200).build()
      )
      .when(
        WithdrawMoneyCommand.builder().accountId(ACCOUNT_ID).amount(50).build()
      )
      .expectEvents(
        MoneyWithdrawnEvent.builder().accountId(ACCOUNT_ID).amount(50).build()
      )
      .expectState(is(
          AccountAggregate.builder().accountId(ACCOUNT_ID).currentBalance(150).build()
        )
      )
    ;
  }

  @Test
  void withdraw_amount_insufficientBalance() {
    fixture
      .given(
        BankAccountCreatedEvent.builder().accountId(ACCOUNT_ID).customerId(CUSTOMER_ID).initialBalance(100).build()
      )
      .when(
        WithdrawMoneyCommand.builder().accountId(ACCOUNT_ID).amount(200).build()
      )
      .expectException(
        InsufficientBalanceException.class
      )
      .expectExceptionMessage(
        "Insufficient balance, was:100, required:200"
      )
    ;
  }


  @Test
  void deposit_money() {
    fixture
      .given(BankAccountCreatedEvent.builder().accountId(ACCOUNT_ID).customerId(CUSTOMER_ID).initialBalance(0).build()
      )
    .when(
      DepositMoneyCommand.builder().accountId(ACCOUNT_ID).amount(100).build()
    )
    .expectEvents(MoneyDepositedEvent.builder().accountId(ACCOUNT_ID).amount(100).build())
    .expectState(is(
      AccountAggregate.builder().accountId(ACCOUNT_ID).currentBalance(100).build()
    ))
      ;
  }


  private Consumer<AccountAggregate> is(AccountAggregate expected) {
    return account -> assertThat(account)
      .isEqualTo(
        expected
      );
  }
}

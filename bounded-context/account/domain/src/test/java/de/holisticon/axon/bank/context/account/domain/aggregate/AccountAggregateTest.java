package de.holisticon.axon.bank.context.account.domain.aggregate;

import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.ACCOUNT_ID_1;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.ACCOUNT_ID_2;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.CUSTOMER_ID_1;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.accountAggregate;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.accountCreatedEvent;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.createAccountCommand;
import static de.holisticon.axon.bank.context.account.domain.aggregate.AccountAggregate.Configuration.DEFAULT_INITIAL_BALANCE;
import static de.holisticon.axon.bank.context.account.domain.aggregate.AccountAggregate.Configuration.DEFAULT_MAXIMAL_BALANCE;
import static org.assertj.core.api.Assertions.assertThat;

import de.holisticon.axon.bank.context.account.domain.aggregate.AccountAggregate.ActiveMoneyTransfer;
import de.holisticon.axon.bank.context.account.domain.api.command.DepositMoneyCommand;
import de.holisticon.axon.bank.context.account.domain.api.command.WithdrawMoneyCommand;
import de.holisticon.axon.bank.context.account.domain.api.command.moneytransfer.InitializeMoneyTransferCommand;
import de.holisticon.axon.bank.context.account.domain.api.event.MoneyDepositedEvent;
import de.holisticon.axon.bank.context.account.domain.api.event.MoneyWithdrawnEvent;
import de.holisticon.axon.bank.context.account.domain.api.event.moneytransfer.MoneyTransferInitializedEvent;
import de.holisticon.axon.bank.context.account.domain.api.exception.InsufficientBalanceException;
import de.holisticon.axon.bank.context.account.domain.api.exception.MaximalBalanceExceededException;
import de.holisticon.axon.bank.context.account.domain.api.exception.MaximumActiveMoneyTransfersReachedException;
import java.util.UUID;
import java.util.function.Consumer;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.Test;

class AccountAggregateTest {

  private final AggregateTestFixture<AccountAggregate> fixture = new AggregateTestFixture<>(AccountAggregate.class);

  @Test
  void create_account() {
    fixture
      .givenNoPriorActivity()
      .when(
        createAccountCommand(ACCOUNT_ID_1, CUSTOMER_ID_1)
      )
      .expectEvents(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1)
      )
      .expectState(is(AccountAggregate.builder()
        .accountId(ACCOUNT_ID_1)
        .maximalBalance(DEFAULT_MAXIMAL_BALANCE)
        .currentBalance(DEFAULT_INITIAL_BALANCE)
        .build())
      )
    ;
  }

  @Test
  void withdraw_amount() {
    fixture
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1).toBuilder()
          .initialBalance(200)
          .build()
      )
      .when(
        WithdrawMoneyCommand.builder().accountId(ACCOUNT_ID_1).amount(50).build()
      )
      .expectEvents(
        MoneyWithdrawnEvent.builder().accountId(ACCOUNT_ID_1).amount(50).build()
      )
      .expectState(
        is(accountAggregate(ACCOUNT_ID_1, 150, DEFAULT_MAXIMAL_BALANCE))
      )
    ;
  }

  @Test
  void withdraw_amount_insufficientBalance() {
    fixture
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1).toBuilder()
          .initialBalance(100)
          .build()
      )
      .when(
        WithdrawMoneyCommand.builder().accountId(ACCOUNT_ID_1).amount(200).build()
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
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1)
      )
      .when(
        DepositMoneyCommand.builder().accountId(ACCOUNT_ID_1).amount(100).build()
      )
      .expectEvents(MoneyDepositedEvent.builder().accountId(ACCOUNT_ID_1).amount(100).build())
      .expectState(is(
        accountAggregate(ACCOUNT_ID_1, 100, DEFAULT_MAXIMAL_BALANCE)
      ))
    ;
  }

  @Test
  void we_must_not_deposit_more_than_maximalBalance() {
    fixture
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1)
          .toBuilder().maximalBalance(100).build()
      )
      .when(
        DepositMoneyCommand.builder()
          .accountId(ACCOUNT_ID_1)
          .amount(101)
          .build()
      )
      .expectException(
        MaximalBalanceExceededException.class
      )
      .expectExceptionMessage(
        "MaximalBalance exceeded: currentBalance=0, amount=101, maximalBalance=100"
      )
    ;

  }

  @Test
  void cannot_have_more_than_one_active_transfer() {
    String transactionId = UUID.randomUUID().toString();
    fixture
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1).toBuilder()
          .initialBalance(1000).build(),
        MoneyTransferInitializedEvent.builder()
          .transactionId(transactionId)
          .targetAccountId(ACCOUNT_ID_2)
          .sourceAccountId(ACCOUNT_ID_1)
          .amount(100)
          .build()
      )
      .when(
        InitializeMoneyTransferCommand.builder()
          .sourceAccountId(ACCOUNT_ID_1)
          .targetAccountId(ACCOUNT_ID_2)
          .amount(100)
          .build()
      )
      .expectException(MaximumActiveMoneyTransfersReachedException.class);
  }

  @Test
  void cannot_initialize_moneyTransfer_on_insufficient_balance() {
    String transactionId = UUID.randomUUID().toString();
    fixture
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1).toBuilder()
          .initialBalance(100).build()
      )
      .when(
        InitializeMoneyTransferCommand.builder()
          .sourceAccountId(ACCOUNT_ID_1)
          .targetAccountId(ACCOUNT_ID_2)
          .transactionId(transactionId)
          .amount(200)
          .build()
      )
      .expectException(InsufficientBalanceException.class)
    ;
  }

  @Test
  void initialize_moneyTransfer() {
    String transactionId = UUID.randomUUID().toString();
    fixture
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1).toBuilder()
          .initialBalance(100).build()
      )
      .when(
        InitializeMoneyTransferCommand.builder()
          .sourceAccountId(ACCOUNT_ID_1)
          .targetAccountId(ACCOUNT_ID_2)
          .transactionId(transactionId)
          .amount(50)
          .build()
      )
      .expectEvents(
        MoneyTransferInitializedEvent.builder()
          .sourceAccountId(ACCOUNT_ID_1)
          .targetAccountId(ACCOUNT_ID_2)
          .transactionId(transactionId)
          .amount(50)
          .build()
      )
      .expectState(a -> assertThat(a.getActiveMoneyTransfer())
        .isEqualTo(ActiveMoneyTransfer.builder().amount(50).transactionId(transactionId).build()))
    ;
  }

  @Test
  void cannot_withdraw_if_amount_reserved_for_active_tranfer() {
    String transactionId = UUID.randomUUID().toString();
    fixture
      .given(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1).toBuilder()
          .initialBalance(100).build(),
        MoneyTransferInitializedEvent.builder().sourceAccountId(ACCOUNT_ID_1).targetAccountId(ACCOUNT_ID_2).transactionId(transactionId)
          .amount(50).build()
      )
      .when(WithdrawMoneyCommand.builder().accountId(ACCOUNT_ID_1).amount(100).build())
      .expectException(InsufficientBalanceException.class)
    ;
  }


  private Consumer<AccountAggregate> is(AccountAggregate expected) {
    return account -> assertThat(account)
      .isEqualTo(
        expected
      );
  }
}

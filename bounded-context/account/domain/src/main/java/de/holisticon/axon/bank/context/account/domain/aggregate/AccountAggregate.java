package de.holisticon.axon.bank.context.account.domain.aggregate;

import static java.util.Optional.ofNullable;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import de.holisticon.axon.bank.context.account.api.command.CreateAccountCommand;
import de.holisticon.axon.bank.context.account.api.command.DepositMoneyCommand;
import de.holisticon.axon.bank.context.account.api.command.WithdrawMoneyCommand;
import de.holisticon.axon.bank.context.account.api.command.moneytransfer.CompleteMoneyTransferCommand;
import de.holisticon.axon.bank.context.account.api.command.moneytransfer.InitializeMoneyTransferCommand;
import de.holisticon.axon.bank.context.account.api.command.moneytransfer.ReceiveMoneyTransferCommand;
import de.holisticon.axon.bank.context.account.api.command.moneytransfer.RollBackMoneyTransferCommand;
import de.holisticon.axon.bank.context.account.api.domainevent.BalanceChangedEvent;
import de.holisticon.axon.bank.context.account.api.event.AccountCreatedEvent;
import de.holisticon.axon.bank.context.account.api.event.MoneyDepositedEvent;
import de.holisticon.axon.bank.context.account.api.event.MoneyWithdrawnEvent;
import de.holisticon.axon.bank.context.account.api.event.moneytransfer.MoneyTransferCompletedEvent;
import de.holisticon.axon.bank.context.account.api.event.moneytransfer.MoneyTransferInitializedEvent;
import de.holisticon.axon.bank.context.account.api.event.moneytransfer.MoneyTransferReceivedEvent;
import de.holisticon.axon.bank.context.account.api.event.moneytransfer.MoneyTransferRolledBackEvent;
import de.holisticon.axon.bank.context.account.api.exception.InsufficientBalanceException;
import de.holisticon.axon.bank.context.account.api.exception.MaximalBalanceExceededException;
import de.holisticon.axon.bank.context.account.api.exception.MaximumActiveMoneyTransfersReachedException;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

@Aggregate
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Slf4j
public class AccountAggregate {

  public enum Configuration {
    ;

    public static final int DEFAULT_INITIAL_BALANCE = 0;
    public static final int DEFAULT_MAXIMAL_BALANCE = 1000;
    public static final int MAXIMUM_NUMBER_OF_ACTIVE_MONEY_TRANSFERS = 1;

  }

  @AggregateIdentifier
  private String accountId;

  private Integer currentBalance;

  private Integer maximalBalance;

  private ActiveMoneyTransfer activeMoneyTransfer;

  @CommandHandler
  public static AccountAggregate handle(CreateAccountCommand cmd) {
    apply(AccountCreatedEvent.builder()
      .accountId(cmd.getAccountId())
      .customerId(cmd.getCustomerId())
      .initialBalance(ofNullable(cmd.getInitialBalance()).orElse(Configuration.DEFAULT_INITIAL_BALANCE))
      .maximalBalance(ofNullable(cmd.getMaximalBalance()).orElse(Configuration.DEFAULT_MAXIMAL_BALANCE))
      .build());

    return new AccountAggregate();
  }

  @EventSourcingHandler
  void on(AccountCreatedEvent evt) {
    log.info("replaying event: {}", evt);
    this.accountId = evt.getAccountId();
    this.currentBalance = evt.getInitialBalance();
    this.maximalBalance = evt.getMaximalBalance();
  }


  @CommandHandler
  void handle(WithdrawMoneyCommand cmd) {
    checkForInsufficientBalance(cmd.getAmount());

    apply(
      MoneyWithdrawnEvent.builder()
        .accountId(accountId)
        .amount(cmd.getAmount())
        .build()
    );
  }

  @EventSourcingHandler
  void on(MoneyWithdrawnEvent evt) {
    log.info("replaying event: {}", evt);
    decreaseCurrentBalance(evt.getAmount());
  }

  @CommandHandler
  void handle(DepositMoneyCommand cmd) {
    checkForMaximalBalanceExceeded(cmd.getAmount());

    apply(
      MoneyDepositedEvent.builder()
        .accountId(accountId)
        .amount(cmd.getAmount())
        .build()
    );
  }


  @EventSourcingHandler
  void on(MoneyDepositedEvent evt) {
    log.info("replaying event: {}", evt);
    increaseCurrentBalance(evt.getAmount());
  }


  @CommandHandler
  void handle(InitializeMoneyTransferCommand cmd) {
    checkForActiveMoneyTransfer();
    checkForInsufficientBalance(cmd.getAmount());

    apply(MoneyTransferInitializedEvent.builder()
      .transactionId(cmd.getTransactionId())
      .sourceAccountId(cmd.getSourceAccountId())
      .targetAccountId(cmd.getTargetAccountId())
      .amount(cmd.getAmount())
      .build());
  }

  @EventSourcingHandler
  void on(MoneyTransferInitializedEvent evt) {
    log.info("replaying event: {}", evt);
    this.activeMoneyTransfer = ActiveMoneyTransfer.builder()
      .transactionId(evt.getTransactionId())
      .amount(evt.getAmount())
      .build();
  }

  @CommandHandler
  void handle(ReceiveMoneyTransferCommand cmd) {
    checkForMaximalBalanceExceeded(cmd.getAmount());

    apply(MoneyTransferReceivedEvent.builder()
      .sourceAccountId(cmd.getSourceAccountId())
      .targetAccountId(cmd.getTargetAccountId())
      .transactionId(cmd.getTransactionId())
      .amount(cmd.getAmount())
      .build()
    );
  }

  @EventSourcingHandler
  void on(MoneyTransferReceivedEvent evt) {
    log.info("replaying event: {}", evt);
    increaseCurrentBalance(evt.getAmount());
  }

  @CommandHandler
  void handle(CompleteMoneyTransferCommand cmd) {
    if (activeMoneyTransfer == null || !activeMoneyTransfer.transactionId.equals(cmd.getTransactionId())) {
      throw new IllegalStateException("not participating in transaction: " + cmd.getTransactionId());
    }

    apply(MoneyTransferCompletedEvent.builder()
      .sourceAccountId(cmd.getSourceAccountId())
      .transactionId(cmd.getTransactionId())
      .amount(cmd.getAmount())
      .build()
    );
  }

  @EventSourcingHandler
  void on(MoneyTransferCompletedEvent evt) {
    decreaseCurrentBalance(evt.getAmount());
    activeMoneyTransfer = null;
  }


  @CommandHandler
  void handle(RollBackMoneyTransferCommand cmd) {
    if (activeMoneyTransfer == null || !activeMoneyTransfer.transactionId.equals(cmd.getTransactionId())) {
      throw new IllegalStateException("not participating in transaction: " + cmd.getTransactionId());
    }

    apply(MoneyTransferRolledBackEvent.builder()
      .sourceAccountId(cmd.getSourceAccountId())
      .transactionId(cmd.getTransactionId())
      .build()
    );
  }


  @EventSourcingHandler
  void on(MoneyTransferRolledBackEvent evt) {
    activeMoneyTransfer = null;
  }


  /**
   * @return stored current balance minus amount(s) reserved by active money transfers
   */
  public int getEffectiveCurrentBalance() {
    return currentBalance - ofNullable(activeMoneyTransfer).map(ActiveMoneyTransfer::getAmount).orElse(0);
  }

  private void increaseCurrentBalance(int amount) {
    this.currentBalance += amount;
    apply(BalanceChangedEvent.builder()
      .accountId(accountId)
      .newBalance(this.currentBalance)
      .build());
  }

  private void decreaseCurrentBalance(int amount) {
    increaseCurrentBalance(-amount);
  }

  private void checkForMaximalBalanceExceeded(int amount) {
    if (maximalBalance < currentBalance + amount) {
      throw new MaximalBalanceExceededException(currentBalance, amount, maximalBalance);
    }
  }

  private void checkForActiveMoneyTransfer() {
    if (activeMoneyTransfer != null) {
      throw new MaximumActiveMoneyTransfersReachedException(Configuration.MAXIMUM_NUMBER_OF_ACTIVE_MONEY_TRANSFERS,
        Collections.singletonList(activeMoneyTransfer.transactionId));
    }
  }

  private void checkForInsufficientBalance(int amount) {
    if (getEffectiveCurrentBalance() < amount) {
      throw new InsufficientBalanceException(getEffectiveCurrentBalance(), amount);
    }

  }


  @Value
  @Builder
  public static class ActiveMoneyTransfer {

    @NonNull
    private String transactionId;
    private int amount;
  }
}

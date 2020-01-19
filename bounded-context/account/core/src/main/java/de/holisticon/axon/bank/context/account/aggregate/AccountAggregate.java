package de.holisticon.axon.bank.context.account.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import de.holisticon.axon.bank.context.account.command.CreateBankAccountCommand;
import de.holisticon.axon.bank.context.account.command.DepositMoneyCommand;
import de.holisticon.axon.bank.context.account.command.WithdrawMoneyCommand;
import de.holisticon.axon.bank.context.account.event.MoneyDepositedEvent;
import de.holisticon.axon.bank.context.account.event.MoneyWithdrawnEvent;
import de.holisticon.axon.bank.context.account.event.BankAccountCreatedEvent;
import de.holisticon.axon.bank.context.account.exception.InsufficientBalanceException;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.serialization.Revision;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
@AllArgsConstructor
@Revision("1")
@Data
@Builder
public class AccountAggregate {

  @AggregateIdentifier
  private String accountId;

  private Integer currentBalance;

  @CommandHandler
  public static AccountAggregate handle(CreateBankAccountCommand cmd) {
    apply(BankAccountCreatedEvent.builder()
      .accountId(cmd.getAccountId())
      .customerId(cmd.getCustomerId())
      .initialBalance(cmd.getInitialBalance())
      .build());
    return new AccountAggregate();
  }

  @EventSourcingHandler
  void on(BankAccountCreatedEvent evt)  {
    this.accountId = evt.getAccountId();
    this.currentBalance = evt.getInitialBalance();
  }


  @CommandHandler
  void handle(WithdrawMoneyCommand cmd) {
    if (currentBalance < cmd.getAmount()) {
      throw new InsufficientBalanceException(currentBalance, cmd.getAmount());
    }

    apply(
      MoneyWithdrawnEvent.builder()
        .accountId(accountId)
        .amount(cmd.getAmount())
        .build()
    );
  }

  @EventSourcingHandler
  void on(MoneyWithdrawnEvent evt) {
    this.currentBalance -= evt.getAmount();
  }

  @CommandHandler
  void handle(DepositMoneyCommand cmd) {
    apply(
      MoneyDepositedEvent.builder()
        .accountId(accountId)
        .amount(cmd.getAmount())
        .build()
    );
  }

  @EventSourcingHandler
  void on(MoneyDepositedEvent evt) {
    this.currentBalance += evt.getAmount();
  }
}

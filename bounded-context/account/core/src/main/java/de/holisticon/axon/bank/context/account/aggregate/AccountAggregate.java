package de.holisticon.axon.bank.context.account.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import de.holisticon.axon.bank.context.account.command.CreateBankAccountCommand;
import de.holisticon.axon.bank.context.account.event.BankAccountCreatedEvent;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.serialization.Revision;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Revision("1")
public class AccountAggregate {

  @AggregateIdentifier
  private String accountId;

  private BigDecimal currentBalance;

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
}

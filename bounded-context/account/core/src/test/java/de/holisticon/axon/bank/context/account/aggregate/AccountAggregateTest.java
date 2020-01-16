package de.holisticon.axon.bank.context.account.aggregate;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import de.holisticon.axon.bank.context.account.command.CreateBankAccountCommand;
import de.holisticon.axon.bank.context.account.event.BankAccountCreatedEvent;
import io.toolisticon.addons.axon.jgiven.aggregate.AggregateFixtureGiven;
import io.toolisticon.addons.axon.jgiven.aggregate.AggregateFixtureThen;
import io.toolisticon.addons.axon.jgiven.aggregate.AggregateFixtureWhen;
import java.math.BigDecimal;
import java.util.UUID;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.Test;

class AccountAggregateTest extends ScenarioTest<AggregateFixtureGiven<AccountAggregate>, AggregateFixtureWhen<AccountAggregate>, AggregateFixtureThen<AccountAggregate>> {

  @ProvidedScenarioState
  private final AggregateTestFixture<AccountAggregate> fixture = new AggregateTestFixture<>(AccountAggregate.class);

  private static final String ACCOUNT_ID = UUID.randomUUID().toString();
  private static final String CUSTOMER_ID = UUID.randomUUID().toString();

  @Test
  void create_account() {
    given()
      .noPriorActivity()
      ;
    when()
      .command(CreateBankAccountCommand.builder().accountId(ACCOUNT_ID).customerId(CUSTOMER_ID).build())
      ;
    then()
      .expectEvent(BankAccountCreatedEvent.builder().accountId(ACCOUNT_ID).customerId(CUSTOMER_ID).initialBalance(BigDecimal.ZERO).build());
  }
}

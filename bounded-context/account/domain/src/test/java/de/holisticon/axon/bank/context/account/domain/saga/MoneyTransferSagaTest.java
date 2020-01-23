package de.holisticon.axon.bank.context.account.domain.saga;

import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.ACCOUNT_ID_1;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.ACCOUNT_ID_2;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.CUSTOMER_ID_1;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.CUSTOMER_ID_2;
import static de.holisticon.axon.bank.context.account.domain.AccountAggregateTestHelper.accountCreatedEvent;

import de.holisticon.axon.bank.context.account.api.event.moneytransfer.MoneyTransferInitializedEvent;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.Test;

class MoneyTransferSagaTest {

  private final SagaTestFixture<MoneyTransferSaga> fixture = new SagaTestFixture<>(MoneyTransferSaga.class);


  @Test
  void transfer_money() {
    fixture
      .givenAggregate(ACCOUNT_ID_1)
      .published(
        accountCreatedEvent(ACCOUNT_ID_1, CUSTOMER_ID_1).toBuilder()
          .initialBalance(2000).build()
      )

      .andThenAggregate(ACCOUNT_ID_2)
      .published(accountCreatedEvent(ACCOUNT_ID_2, CUSTOMER_ID_2))

      .whenAggregate(ACCOUNT_ID_1)
      .publishes(MoneyTransferInitializedEvent.builder()
        .sourceAccountId(ACCOUNT_ID_1)
        .targetAccountId(ACCOUNT_ID_2)
        .amount(100)
        .build()
      )
      .expectActiveSagas(1)
    ;

  }


}

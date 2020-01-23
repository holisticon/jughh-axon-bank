package de.holisticon.axon.bank.context.account.domain.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

class AccountAggregateTest {




  private Consumer<AccountAggregate> is(AccountAggregate expected) {
    return account -> assertThat(account)
      .isEqualTo(
        expected
      );
  }
}

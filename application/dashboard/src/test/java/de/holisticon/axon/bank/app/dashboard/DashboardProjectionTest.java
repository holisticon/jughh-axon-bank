package de.holisticon.axon.bank.app.dashboard;

import static org.assertj.core.api.Assertions.assertThat;

import de.holisticon.axon.bank.app.dashboard.DashboardProjection.CustomerAccountDashboardDto;
import de.holisticon.axon.bank.context.account.api.event.AccountCreatedEvent;
import de.holisticon.axon.bank.context.customer.api.event.CustomerRegisteredEvent;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DashboardProjectionTest {

  private static final String CUSTOMER_ID = UUID.randomUUID().toString();

  private final DashboardProjection projection = new DashboardProjection();


  @Test
  void initially_empty() {
    assertThat(projection.findByCustomerId(CUSTOMER_ID)).isEmpty();
  }

  @Test
  void customerRegistered_creates_entry() {
    projection.on(CustomerRegisteredEvent.builder()
      .name("Jan")
      .customerId(CUSTOMER_ID)
      .build());

    Optional<CustomerAccountDashboardDto> customer = projection.findByCustomerId(CUSTOMER_ID);
    assertThat(customer).isNotEmpty();
    assertThat(customer.get().getName()).isEqualTo("Jan");
    assertThat(customer.get().getAccounts()).isEmpty();

  }

  @Test
  void accountCreated_adds_to_list() {
    projection.on(CustomerRegisteredEvent.builder()
      .name("Jan")
      .customerId(CUSTOMER_ID)
      .build());

    projection.on(AccountCreatedEvent.builder()
      .accountId("a1")
      .customerId(CUSTOMER_ID)
      .initialBalance(100)
      .maximalBalance(1000)
      .build());

    Optional<CustomerAccountDashboardDto> customer = projection.findByCustomerId(CUSTOMER_ID);
    assertThat(customer).isNotEmpty();
    assertThat(customer.get().getName()).isEqualTo("Jan");
    assertThat(customer.get().getAccounts()).hasSize(1);
    assertThat(customer.get().getAccounts().get("a1").getCurrentBalance()).isEqualTo(100);
  }
}

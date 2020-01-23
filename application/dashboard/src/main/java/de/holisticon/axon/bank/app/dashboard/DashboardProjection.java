package de.holisticon.axon.bank.app.dashboard;

import de.holisticon.axon.bank.context.account.api.domainevent.BalanceChangedEvent;
import de.holisticon.axon.bank.context.account.api.event.AccountCreatedEvent;
import de.holisticon.axon.bank.context.customer.api.event.CustomerRegisteredEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DashboardProjection {

  private final Map<String, CustomerAccountDashboardDto> store = new ConcurrentHashMap<>();

  public Optional<CustomerAccountDashboardDto> findByCustomerId(String customerId) {
    return Optional.ofNullable(store.get(customerId));
  }

  @EventHandler
  void on(CustomerRegisteredEvent evt) {
    store.put(evt.getCustomerId(), CustomerAccountDashboardDto.builder()
      .customerId(evt.getCustomerId())
      .name(evt.getName())
      .build());
    logState();
  }

  @EventHandler
  void on(AccountCreatedEvent evt) {
    store.computeIfPresent(evt.getCustomerId(), (customerId, dto) -> {

      dto.accounts.put(evt.getAccountId(), new AccountDto(evt.getAccountId(), evt.getInitialBalance()));
      return dto;
    });
    logState();
  }

  @EventHandler
  void on(BalanceChangedEvent evt) {
    CustomerAccountDashboardDto dto = findByAccountId(evt.getAccountId()).orElseThrow(IllegalStateException::new);

    dto.getAccounts().put(evt.getAccountId(), new AccountDto(evt.getAccountId(), evt.getNewBalance()));

    logState();
  }


  private Optional<CustomerAccountDashboardDto> findByAccountId(String accountId) {
    return store.values().stream().filter(dto -> dto.getAccounts().containsKey(accountId)).findFirst();
  }

  private void logState() {
    log.info("accounts: {}", store);
  }

  @Data
  @Builder
  public static class CustomerAccountDashboardDto {

    private String customerId;

    private String name;

    @Builder.Default
    private Map<String, AccountDto> accounts = new HashMap<>();
  }

  @Data
  @AllArgsConstructor
  public static class AccountDto {

    private String accountId;
    private int currentBalance;
  }

}

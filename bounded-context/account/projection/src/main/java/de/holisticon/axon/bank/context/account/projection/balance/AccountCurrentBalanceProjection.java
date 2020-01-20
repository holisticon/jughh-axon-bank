package de.holisticon.axon.bank.context.account.projection.balance;

import de.holisticon.axon.bank.context.account.domain.api.event.BankAccountCreatedEvent;
import de.holisticon.axon.bank.context.account.domain.api.event.MoneyDepositedEvent;
import de.holisticon.axon.bank.context.account.domain.api.event.MoneyWithdrawnEvent;
import de.holisticon.axon.bank.context.account.domain.api.query.AccountCurrentBalanceDto;
import de.holisticon.axon.bank.context.account.domain.api.query.AccountFindByIdQuery;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;

@RequiredArgsConstructor
public class AccountCurrentBalanceProjection {

  private final Map<String, AccountCurrentBalanceDto> store = new ConcurrentHashMap<>();

  @EventHandler
  public void on(BankAccountCreatedEvent evt) {
    store.put(evt.getAccountId(), AccountCurrentBalanceDto.builder()
      .accountId(evt.getAccountId())
      .currentBalance(evt.getInitialBalance())
      .build());
  }

  @EventHandler
  public void on(MoneyDepositedEvent evt) {
    update(evt.getAccountId(), evt.getAmount());
  }

  @EventHandler
  public void on(MoneyWithdrawnEvent evt) {
    update(evt.getAccountId(), -evt.getAmount());
  }

  private void update(String accountId, int amount) {
    store.computeIfPresent(accountId, (id, dto) -> dto.toBuilder().currentBalance(dto.getCurrentBalance() + amount).build());
  }

  @QueryHandler
  public Optional<AccountCurrentBalanceDto> query(AccountFindByIdQuery query) {
    return Optional.ofNullable(store.get(query.getAccountId()));
  }
}

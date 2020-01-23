package de.holisticon.axon.bank.context.account.projection.balance;

import de.holisticon.axon.bank.context.account.api.event.AccountCreatedEvent;
import de.holisticon.axon.bank.context.account.api.event.MoneyDepositedEvent;
import de.holisticon.axon.bank.context.account.api.event.MoneyWithdrawnEvent;
import de.holisticon.axon.bank.context.account.api.event.moneytransfer.MoneyTransferCompletedEvent;
import de.holisticon.axon.bank.context.account.api.event.moneytransfer.MoneyTransferReceivedEvent;
import de.holisticon.axon.bank.context.account.api.query.AccountCurrentBalanceDto;
import de.holisticon.axon.bank.context.account.api.query.AccountFindAllQuery;
import de.holisticon.axon.bank.context.account.api.query.AccountFindByIdQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

@RequiredArgsConstructor
public class AccountCurrentBalanceProjection {

  private final Map<String, AccountCurrentBalanceDto> store = new ConcurrentHashMap<>();

  @EventHandler
  public void on(AccountCreatedEvent evt) {
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

  @EventHandler
  void on(MoneyTransferReceivedEvent evt) {
    update(evt.getTargetAccountId(), evt.getAmount());
  }

  @EventHandler
  void on(MoneyTransferCompletedEvent evt) {
    update(evt.getSourceAccountId(), -evt.getAmount());
  }



  private void update(String accountId, int amount) {
    store.computeIfPresent(accountId, (id, dto) -> dto.toBuilder().currentBalance(dto.getCurrentBalance() + amount).build());
  }

  @QueryHandler
  public Optional<AccountCurrentBalanceDto> query(AccountFindByIdQuery query) {
    return Optional.ofNullable(store.get(query.getAccountId()));
  }

  @QueryHandler
  public List<AccountCurrentBalanceDto> query(AccountFindAllQuery query) {
    return new ArrayList<>(store.values());
  }
}

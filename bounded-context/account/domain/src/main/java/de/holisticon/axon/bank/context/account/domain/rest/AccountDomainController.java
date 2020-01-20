package de.holisticon.axon.bank.context.account.domain.rest;

import de.holisticon.axon.bank.context.account.domain.api.command.CreateBankAccountCommand;
import de.holisticon.axon.bank.context.account.domain.api.command.DepositMoneyCommand;
import de.holisticon.axon.bank.context.account.domain.api.command.WithdrawMoneyCommand;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/account/domain")
@RequiredArgsConstructor
public class AccountDomainController {

  private final CommandGateway commandGateway;

  @PostMapping("/")
  public ResponseEntity<String> createAccount(
    @RequestParam(value = "accountId", required = false) String accountId,
    @RequestParam("customerId") String customerId,
    @RequestParam(value = "initialBalance", required = false) Integer initialBalance
  ) {
    String createdAccount = commandGateway.sendAndWait(CreateBankAccountCommand.builder()
      .accountId(Optional.ofNullable(accountId).orElse(UUID.randomUUID().toString()))
      .customerId(customerId)
      .initialBalance(Optional.ofNullable(initialBalance).orElse(0))
      .build());

    return ResponseEntity.ok("{\"accountCreated\":\"" + createdAccount + "\"}");
  }

  @PutMapping("/{accountId}/deposit")
  public ResponseEntity<String> depositMoney(
    @PathVariable("accountId") String accountId,
    @RequestParam("amount") int amount
  ) {
    commandGateway.send(DepositMoneyCommand.builder()
      .accountId(accountId)
      .amount(amount)
      .build()
    );
    return ResponseEntity.ok("{\"moneyDeposited\":\"" + amount + "\"}");
  }

  @PutMapping("/{accountId}/withdraw")
  public ResponseEntity<String> withdrawMoney(
    @PathVariable("accountId") String accountId,
    @RequestParam("amount") int amount
  ) {

    final WithdrawMoneyCommand command = WithdrawMoneyCommand.builder()
      .accountId(accountId)
      .amount(amount)
      .build();

    try {
      commandGateway.sendAndWait(command);
      return ResponseEntity.ok("{\"moneyDeposited\":\"" + amount + "\"}");
    } catch (CommandExecutionException e) {
      return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

}

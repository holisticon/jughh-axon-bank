package de.holisticon.axon.bank.context.account.domain.rest;

import de.holisticon.axon.bank.context.account.api.command.CreateAccountCommand;
import de.holisticon.axon.bank.context.account.api.command.CreateAccountCommand.CreateAccountCommandBuilder;
import de.holisticon.axon.bank.context.account.api.command.DepositMoneyCommand;
import de.holisticon.axon.bank.context.account.api.command.WithdrawMoneyCommand;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/account/domain")
@RequiredArgsConstructor
@CrossOrigin
public class AccountDomainController {

  private final CommandGateway commandGateway;

  @PostMapping("/")
  public ResponseEntity<String> createAccount(
    @RequestParam(value = "accountId", required = false) String accountId,
    @RequestParam("customerId") String customerId,
    @RequestParam(value = "initialBalance", required = false) Integer initialBalance,
    @RequestParam(value = "maximalBalance", required = false) Integer maximalBalance
  ) {

    CreateAccountCommandBuilder builder = CreateAccountCommand.builder()
      .accountId(Optional.ofNullable(accountId).orElse(UUID.randomUUID().toString()))
      .customerId(customerId);

    if (initialBalance != null) {
      builder.initialBalance(initialBalance);
    }
    if (maximalBalance != null) {
      builder.maximalBalance(maximalBalance);
    }

    try {
      String createdAccount = commandGateway.sendAndWait(builder.build());
      return ResponseEntity.ok("{\"accountCreated\":\"" + createdAccount + "\"}");
    } catch (CommandExecutionException e) {
      return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @PutMapping("/{accountId}/deposit")
  public ResponseEntity<String> depositMoney(
    @PathVariable("accountId") String accountId,
    @RequestParam("amount") int amount
  ) {
    try {
      commandGateway.send(DepositMoneyCommand.builder()
        .accountId(accountId)
        .amount(amount)
        .build()
      );
      return ResponseEntity.ok("{\"moneyDeposited\":\"" + amount + "\"}");
    } catch (CommandExecutionException e) {
      return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
    }
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
//
//  @PostMapping("/moneytransfer")
//  public ResponseEntity<String> initMoneyTransfer(
//    @RequestParam("sourceAccountId") String sourceAccountId,
//    @RequestParam("targetAccountId") String targetAccountId,
//    @RequestParam("amount") int amount
//  ) {
//    String transactionId = UUID.randomUUID().toString();
//    try {
//      commandGateway.sendAndWait(InitializeMoneyTransferCommand.builder()
//        .sourceAccountId(sourceAccountId)
//        .targetAccountId(targetAccountId)
//        .amount(amount)
//        .transactionId(transactionId)
//        .build());
//      return ResponseEntity.ok("{\"moneyTransferInitialized\":\"" + transactionId + "\"}");
//    } catch (CommandExecutionException e) {
//      return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
//    }
//  }


}

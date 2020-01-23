package de.holisticon.axon.bank.context.customer.domain.rest;

import de.holisticon.axon.bank.context.customer.api.command.RegisterCustomerCommand;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/customer/domain")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerDomainController {

  private final CommandGateway commandGateway;

  @PostMapping("/")
  public ResponseEntity<String> createAccount(
    @RequestParam(value = "customerId", required = false) String customerId,
    @RequestParam("name") String name
  ) {
    RegisterCustomerCommand cmd = RegisterCustomerCommand.builder()
      .customerId(Optional.ofNullable(customerId).orElse(UUID.randomUUID().toString()))
      .name(name)
      .build();

    try {
      String createdCustomer = commandGateway.sendAndWait(cmd);
      return ResponseEntity.ok("{\"accountCreated\":\"" + createdCustomer + "\"}");
    } catch (CommandExecutionException e) {
      return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}

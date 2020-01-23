package de.holisticon.axon.bank.app;

import static org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf;

import de.holisticon.axon.bank.context.account.api.query.AccountCurrentBalanceDto;
import de.holisticon.axon.bank.context.account.api.query.AccountFindByCustomerIdQuery;
import de.holisticon.axon.bank.context.customer.api.query.CustomerFindAllQuery;
import de.holisticon.axon.bank.context.customer.api.query.CustomerFindByIdQuery;
import de.holisticon.axon.bank.context.customer.api.query.CustomerInfoDto;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/dashboard")
@Slf4j
@CrossOrigin
public class AxonBankDashboardApplication {

  public static void main(String[] args) {
    SpringApplication.run(AxonBankDashboardApplication.class, args);
  }

  @Autowired
  private QueryGateway queryGateway;


  private List<CustomerInfoDto> findAllCustomers() {
    return queryGateway.query(
      CustomerFindAllQuery.of(),
      multipleInstancesOf(CustomerInfoDto.class)
    ).join();
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<CustomerAccountDashboardDto> findByCustomerId(@PathVariable("customerId") String customerId) {
    Optional<CustomerInfoDto> customer = queryGateway
      .query(CustomerFindByIdQuery.of(customerId), ResponseTypes.optionalInstanceOf(CustomerInfoDto.class)).join();

    return customer.map(customerInfoDto -> ResponseEntity.ok(addAccounts(customerInfoDto)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/customer-id")
  public ResponseEntity<Set<String>> findAllIds() {
    return ResponseEntity.ok(
      findAllCustomers()
        .stream()
        .map(CustomerInfoDto::getCustomerId)
        .collect(Collectors.toSet()));
  }

  @GetMapping("/customer")
  public ResponseEntity<Collection<CustomerAccountDashboardDto>> findAll() {
    return ResponseEntity.ok(findAllCustomers()
      .stream()
      .map(this::addAccounts)
      .collect(Collectors.toList()));
  }


  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }


  private CustomerAccountDashboardDto addAccounts(CustomerInfoDto customer) {
    List<AccountCurrentBalanceDto> dtos = queryGateway
      .query(AccountFindByCustomerIdQuery.of(customer.getCustomerId()), multipleInstancesOf(AccountCurrentBalanceDto.class)).join();

    return CustomerAccountDashboardDto.builder()
      .name(customer.getName())
      .customerId(customer.getCustomerId())
      .accounts(dtos.stream().map(it -> new AccountDto(it.getAccountId(), it.getCurrentBalance()))
        .collect(Collectors.toMap(AccountDto::getAccountId, it -> it)))
      .build();
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

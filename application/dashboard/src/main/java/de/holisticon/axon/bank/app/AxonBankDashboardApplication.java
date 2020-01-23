package de.holisticon.axon.bank.app;

import de.holisticon.axon.bank.app.dashboard.DashboardProjection;
import de.holisticon.axon.bank.app.dashboard.DashboardProjection.CustomerAccountDashboardDto;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
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

import java.util.Collection;
import java.util.Set;

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
  private DashboardProjection dashboardProjection;

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<CustomerAccountDashboardDto> findByCustomerId(@PathVariable("customerId") String customerId) {
    return ResponseEntity.of(dashboardProjection.findByCustomerId(customerId));
  }

  @GetMapping("/customer-id")
  public ResponseEntity<Set<String>> findAllIds() {
    return ResponseEntity.ok(dashboardProjection.findAllIds());
  }

  @GetMapping("/customer")
  public ResponseEntity<Collection<CustomerAccountDashboardDto>> findAll() {
    return ResponseEntity.ok(dashboardProjection.findAll());
  }

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }


}

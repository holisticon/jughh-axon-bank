package de.holisticon.axon.bank.app;

import de.holisticon.axon.bank.context.account.domain.AccountDomainConfiguration;
import de.holisticon.axon.bank.context.account.projection.AccountProjectionConfiguration;
import de.holisticon.axon.bank.context.customer.domain.CustomerDomainConfiguration;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
  AccountDomainConfiguration.class,
  AccountProjectionConfiguration.class,
  CustomerDomainConfiguration.class
})
public class AxonBankAllInOneApplication {

  public static void main(String[] args) {
    SpringApplication.run(AxonBankAllInOneApplication.class, args);
  }


  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }
}

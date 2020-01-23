package de.holisticon.axon.bank.context.customer.domain;

import de.holisticon.axon.bank.context.customer.domain.aggregate.CustomerAggregate;
import de.holisticon.axon.bank.context.customer.domain.rest.CustomerDomainController;
import org.springframework.context.annotation.Import;

@Import({
  CustomerAggregate.class,
  CustomerDomainController.class
})
public class CustomerDomainConfiguration {
}

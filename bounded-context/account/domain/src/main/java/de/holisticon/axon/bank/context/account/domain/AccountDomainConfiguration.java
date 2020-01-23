package de.holisticon.axon.bank.context.account.domain;

import de.holisticon.axon.bank.context.account.domain.aggregate.AccountAggregate;
import de.holisticon.axon.bank.context.account.domain.rest.AccountDomainController;
import org.springframework.context.annotation.Import;

@Import({
  AccountAggregate.class,
  AccountDomainController.class,

})
public class AccountDomainConfiguration {
}

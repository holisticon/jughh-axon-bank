package de.holisticon.axon.bank.context.account.projection;

import de.holisticon.axon.bank.context.account.projection.balance.AccountCurrentBalanceProjection;
import de.holisticon.axon.bank.context.account.projection.rest.AccountProjectionController;
import org.springframework.context.annotation.Import;

@Import({
  AccountCurrentBalanceProjection.class,
  AccountProjectionController.class
})
public class AccountProjectionConfiguration {

}

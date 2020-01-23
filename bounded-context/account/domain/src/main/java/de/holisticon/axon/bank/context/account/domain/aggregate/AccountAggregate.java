package de.holisticon.axon.bank.context.account.domain.aggregate;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class AccountAggregate {

  public enum Configuration {
    ;

    public static final int DEFAULT_INITIAL_BALANCE = 0;
    public static final int DEFAULT_MAXIMAL_BALANCE = 1000;
    public static final int MAXIMUM_NUMBER_OF_ACTIVE_MONEY_TRANSFERS = 1;

  }

}

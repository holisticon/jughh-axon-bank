package de.holisticon.axon.bank.context.account.api.query;

import lombok.Value;

@Value(staticConstructor = "of")
public class AccountFindByIdQuery {

  private String accountId;
}

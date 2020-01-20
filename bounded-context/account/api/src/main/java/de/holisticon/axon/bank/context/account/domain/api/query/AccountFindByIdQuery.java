package de.holisticon.axon.bank.context.account.domain.api.query;

import lombok.Value;

@Value(staticConstructor = "of")
public class AccountFindByIdQuery {

  private String accountId;
}

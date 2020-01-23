package de.holisticon.axon.bank.context.account.api.query;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class AccountFindByIdQuery {

  @NonNull
  private String accountId;
}

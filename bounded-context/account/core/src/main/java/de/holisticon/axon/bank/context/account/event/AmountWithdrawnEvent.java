package de.holisticon.axon.bank.context.account.event;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class AmountWithdrawnEvent {

  @NonNull
  private String accountId;

  @NonNull
  private BigDecimal amount;
}

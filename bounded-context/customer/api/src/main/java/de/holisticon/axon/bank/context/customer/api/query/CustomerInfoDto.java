package de.holisticon.axon.bank.context.customer.api.query;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerInfoDto {

  private String customerId;

  private String name;
}

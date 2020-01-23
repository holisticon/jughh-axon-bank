package de.holisticon.axon.bank.context.customer.projection.info;


import de.holisticon.axon.bank.context.customer.api.event.CustomerRegisteredEvent;
import de.holisticon.axon.bank.context.customer.api.query.CustomerFindAllQuery;
import de.holisticon.axon.bank.context.customer.api.query.CustomerFindByIdQuery;
import de.holisticon.axon.bank.context.customer.api.query.CustomerInfoDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

public class CustomerInfoProjection {

  private final Map<String, CustomerInfoDto> store = new ConcurrentHashMap<>();

  @QueryHandler
  Optional<CustomerInfoDto> query(CustomerFindByIdQuery query) {
    return Optional.ofNullable(store.get(query.getCustomerId()));
  }

  @QueryHandler
  List<CustomerInfoDto> query(CustomerFindAllQuery query) {
    return new ArrayList<>(store.values());
  }

  @EventHandler
  public void on(CustomerRegisteredEvent evt) {
    store.put(evt.getCustomerId(), CustomerInfoDto.builder()
      .customerId(evt.getCustomerId())
      .name(evt.getName())
      .build());
  }
}

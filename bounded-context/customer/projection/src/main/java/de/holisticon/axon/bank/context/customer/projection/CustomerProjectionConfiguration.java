package de.holisticon.axon.bank.context.customer.projection;

import de.holisticon.axon.bank.context.customer.projection.info.CustomerInfoProjection;
import org.springframework.context.annotation.Import;

@Import({
  CustomerInfoProjection.class
})
public class CustomerProjectionConfiguration {

}

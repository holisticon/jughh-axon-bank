package de.holisticon.axon.bank.context.account.projection.rest;

import static org.axonframework.messaging.responsetypes.ResponseTypes.optionalInstanceOf;

import de.holisticon.axon.bank.context.account.domain.api.query.AccountCurrentBalanceDto;
import de.holisticon.axon.bank.context.account.domain.api.query.AccountFindByIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/account/projection")
@RequiredArgsConstructor
public class AccountProjectionController {

  private final QueryGateway queryGateway;

  @GetMapping("/{accountId}")
  public ResponseEntity<AccountCurrentBalanceDto> findCurrentBalanceById(
    @RequestParam("accountId") String accountId
  ) {
    return ResponseEntity.of(
      queryGateway.query(
        AccountFindByIdQuery.of(accountId),
        optionalInstanceOf(AccountCurrentBalanceDto.class))
        .join()
    );
  }

}

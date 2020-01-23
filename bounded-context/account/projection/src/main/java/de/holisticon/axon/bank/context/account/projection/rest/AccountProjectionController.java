package de.holisticon.axon.bank.context.account.projection.rest;

import de.holisticon.axon.bank.context.account.api.query.AccountCurrentBalanceDto;
import de.holisticon.axon.bank.context.account.api.query.AccountFindAllQuery;
import de.holisticon.axon.bank.context.account.api.query.AccountFindByIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf;
import static org.axonframework.messaging.responsetypes.ResponseTypes.optionalInstanceOf;

@RequestMapping("/account/projection")
@RequiredArgsConstructor
@CrossOrigin
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

  @GetMapping("/")
  public ResponseEntity<List<AccountCurrentBalanceDto>> findAll() {
    return ResponseEntity.ok(
      queryGateway.query(
        AccountFindAllQuery.of(),
        multipleInstancesOf(AccountCurrentBalanceDto.class))
                  .join()
    );
  }


}

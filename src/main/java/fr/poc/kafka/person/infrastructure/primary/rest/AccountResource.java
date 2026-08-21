package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.openapi.model.AccountDto;
import fr.poc.kafka.openapi.rest.AccountApi;
import fr.poc.kafka.person.application.AccountApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class AccountResource implements AccountApi {

    private final AccountApplicationService accountApplicationService;

    public AccountResource(AccountApplicationService accountApplicationService) {
        this.accountApplicationService = accountApplicationService;
    }

    @Override
    public ResponseEntity<AccountDto> createOrUpdateAccount(AccountDto accountSk) {
        return new ResponseEntity<>(accountApplicationService.createOrUpdateAccountDto(accountSk), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AccountDto> getAccount(Long id) {
        AccountDto accountDto = accountApplicationService.getAccountDto(id);
        return new ResponseEntity<>(accountDto, accountDto == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AccountDto>> getAccounts() {
        List<AccountDto> accountDto = accountApplicationService.getAccountDtoList();
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }
}

package fr.poc.kafka.person.application;

import fr.poc.kafka.openapi.model.AccountDto;

import java.util.List;

public interface IAccountApplicationService {

    AccountDto getAccountDto(long id);

    List<AccountDto> getAccountDtoList();

    AccountDto createOrUpdateAccountDto(AccountDto AccountDto);
}

package fr.poc.kafka.person.infrastructure.mappers;

import fr.poc.kafka.openapi.model.AccountDto;
import fr.poc.kafka.person.domain.ent.Account;
import fr.poc.kafka.person.infrastructure.secondary.entities.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface AccountMapper {

    Account mapToAccount(AccountDto AccountDto);

    @Mapping(source = "personEntityRO", target = "person")
    Account mapToAccount(AccountEntity AccountEntity);

    AccountDto mapToAccountDto(Account Account);

    @Mapping(source = "person", target = "personEntityRO")
    AccountEntity mapToAccountEntity(Account Account);
}

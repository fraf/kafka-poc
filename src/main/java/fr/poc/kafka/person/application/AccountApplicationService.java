package fr.poc.kafka.person.application;

import fr.poc.kafka.openapi.model.AccountDto;
import fr.poc.kafka.person.domain.ports.primary.IAccountUseCase;
import fr.poc.kafka.person.infrastructure.mappers.AccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Responsable de :
 * <ul>
 * <li>De l'orchestration simple (pas de Métier) des opérations ;</li>
 * <li>De la gestion des transactions ;</li>
 * <li>De la gestion des authorizations (que je fais avec kipe).</li>
 * </ul>
 */
@Service
@Transactional
public class AccountApplicationService implements IAccountApplicationService {

    private final IAccountUseCase accountUseCase;
    private final AccountMapper accountMapper;

    public AccountApplicationService(IAccountUseCase accountUseCase, AccountMapper accountMapper) {
        this.accountUseCase = accountUseCase;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDto getAccountDto(long id) {
        return accountUseCase.getAccount(id).map(accountMapper::mapToAccountDto).orElse(null);
    }

    @Override
    public List<AccountDto> getAccountDtoList() {
        return accountUseCase.getAccountList().stream().map(accountMapper::mapToAccountDto).toList();
    }

    @Override
    public AccountDto createOrUpdateAccountDto(AccountDto accountDto) {
        return accountMapper.mapToAccountDto(accountUseCase.createOrUpdateAccount(accountMapper.mapToAccount(accountDto)));
    }
}

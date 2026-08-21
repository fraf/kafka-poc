package fr.poc.kafka.person.domain;

import fr.poc.kafka.person.domain.ent.Account;
import fr.poc.kafka.person.domain.ports.primary.IAccountUseCase;
import fr.poc.kafka.person.domain.ports.secondary.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class AccountService implements IAccountUseCase {

    @Autowired
    private PasswordEncoder bcryptEncoder;

    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> getAccount(long id) {
        return accountRepository.getById(id);
    }

    @Override
    public List<Account> getAccountList() {
        return accountRepository.getAccountList();
    }

    @Override
    public Account createOrUpdateAccount(Account account) {
        Account accountWithEncryptPassword = account.withPassword(bcryptEncoder.encode(account.password()));
        return accountRepository.saveOrUpdate(accountWithEncryptPassword);

    }

    @Override
    public boolean deleteAccount(long id) {
        return false;
    }
}

package fr.poc.kafka.person.infrastructure.secondary.repository;

import fr.poc.kafka.person.domain.ent.Account;
import fr.poc.kafka.person.domain.ports.secondary.IAccountRepository;
import fr.poc.kafka.person.infrastructure.mappers.AccountMapper;
import fr.poc.kafka.person.infrastructure.mappers.PersonMapper;
import fr.poc.kafka.person.infrastructure.secondary.entities.AccountEntity;
import fr.poc.kafka.person.infrastructure.secondary.entities.PersonEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountPostgresAdapter extends SimpleJpaRepository<AccountEntity, Long> implements IAccountRepository {

    private final AccountMapper accountMapper;
    private final PersonMapper personMapper;
    private final EntityManager entityManager;

    public AccountPostgresAdapter(EntityManager entityManager, AccountMapper accountMapper, PersonMapper personMapper) {
        super(JpaEntityInformationSupport.getEntityInformation(AccountEntity.class, entityManager), entityManager);
        this.entityManager = entityManager;
        this.accountMapper = accountMapper;
        this.personMapper = personMapper;
    }

    @Override
    public List<Account> getAccountList() {
        return super.findAll().stream().map(accountMapper::mapToAccount).toList();
    }

    @Override
    public Optional<Account> getById(long id) {
        return super.findById(id).map(accountMapper::mapToAccount);
    }

    @Override
    public Account saveOrUpdate(Account account) {
        return accountMapper.mapToAccount(super.save(accountMapper.mapToAccountEntity(account)));
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }
}

package fr.poc.kafka.person.domain.ports.secondary;

import fr.poc.kafka.person.domain.ent.Account;
import fr.poc.kafka.person.domain.ent.Person;

import java.util.List;
import java.util.Optional;

/**
 * Domain Layer - Port (Secondary/Driven Port)
 */
public interface IAccountRepository {

    List<Account> getAccountList();

    Optional<Account> getById(long id);

    Account saveOrUpdate(Account account);

    boolean deleteById(long id);
}

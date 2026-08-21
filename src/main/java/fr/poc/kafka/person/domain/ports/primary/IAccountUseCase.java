package fr.poc.kafka.person.domain.ports.primary;

import fr.poc.kafka.person.domain.ent.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountUseCase {

    Optional<Account> getAccount(long id);

    List<Account> getAccountList();

    Account createOrUpdateAccount(Account account);

    boolean deleteAccount(long id);

}

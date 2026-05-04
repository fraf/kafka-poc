package fr.poc.kafka.config;

import fr.poc.kafka.person.domain.PersonService;
import fr.poc.kafka.person.domain.ports.secondary.IPersonEventDispatcher;
import fr.poc.kafka.person.domain.ports.secondary.IPersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>Bounded Context</b> is a central pattern in Domain-Driven Design. It is the focus of DDD's strategic design section which is all about dealing with large models and teams. DDD deals with large models by dividing them into different Bounded Contexts and being explicit about their interrelationships.
 * @see <a href="https://martinfowler.com/bliki/BoundedContext.html?ref=blog.ippon.fr">Bounded Context</a>
 */
@Configuration
public class BoundedContextConfiguration {

    @Bean
    public PersonService personContext(IPersonRepository personRepository, IPersonEventDispatcher personEventDispatcher) {
        return new PersonService(personRepository, personEventDispatcher);
    }



//    @Bean
//    public Flyway flyway(){
//        return new Flyway(Flyway.configure());
//    }
}
package fr.poc.kafka.person.domain.vo;

public record Address(
        String street,
        String city,
        Integer zipcode
) {
}

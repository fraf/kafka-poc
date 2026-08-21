package fr.poc.kafka.person.infrastructure.secondary.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public class Address {
    @Column
    public String street;
    @Column
    public String city;
    @Column
    public Integer zipcode;
}

package fr.poc.kafka.person.infrastructure.secondary.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Address {
    @Column
    private String street;
    @Column
    private String city;
    @Column
    private Integer zipcode;
}

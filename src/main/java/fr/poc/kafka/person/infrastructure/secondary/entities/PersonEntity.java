package fr.poc.kafka.person.infrastructure.secondary.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Entity
@Table(name = "personne")
@Builder
public class PersonEntity extends Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column
    @NotNull
    public String firstname;
    @Column
    @NotNull
    public String lastname;
    @Column
    @NotNull
    public int age;
}

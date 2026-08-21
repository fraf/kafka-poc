package fr.poc.kafka.person.infrastructure.secondary.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "personne")
@SuperBuilder
@NoArgsConstructor //pour JPA
@Setter //pour proxy hibernate
@Getter //pour proxy hibernate
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

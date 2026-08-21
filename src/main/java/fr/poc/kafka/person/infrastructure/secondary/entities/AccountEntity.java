package fr.poc.kafka.person.infrastructure.secondary.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "account")
@Builder
@AllArgsConstructor //pour lombok avec @Builder
@NoArgsConstructor //pour JPA
@Setter
@Getter
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    @NotNull
    public String login;

    @Column
    @NotNull
    public String password;

    //Colonne en ecriture (Insert ou update)
    @Column(name = "id_person")
    private Long personId;

    //Colonne en lecture seule (Select)
    @OneToOne(optional = false)
    @JoinColumn(name = "id_person", referencedColumnName = "id", insertable = false, updatable = false)
    public PersonEntity personEntityRO;
}

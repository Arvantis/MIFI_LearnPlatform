package fun.justdevelops.learnplatform.entity.person;

import fun.justdevelops.learnplatform.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person extends AbstractEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "external_ref", length = 100)
    private String externalRef;

    @Column(name = "birth_date")
    private LocalDate birthDate;
}

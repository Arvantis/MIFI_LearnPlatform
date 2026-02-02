package fun.justdevelops.learnplatform.entity.learning_resources;

import fun.justdevelops.learnplatform.entity.AbstractEntity;
import fun.justdevelops.learnplatform.enums.Complexity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "learning_resources")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "resource_type", discriminatorType = DiscriminatorType.STRING)
public abstract class LearningResource extends AbstractEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexity")
    private Complexity complexity;
}

package fun.justdevelops.learnplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag extends AbstractEntity {
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;
    @Column(name = "slug", unique = true, nullable = false, length = 50)
    private String slug;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();
}
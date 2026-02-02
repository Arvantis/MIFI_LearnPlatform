package fun.justdevelops.learnplatform.entity.quiz;

import fun.justdevelops.learnplatform.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class Question extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "text")
    private String text;

    @Column(name = "type")
    private String type;

    @Setter
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnswerOption> options = new ArrayList<>();

    public Question(Quiz quiz, String text, String type) {
        this.quiz = quiz;
        this.text = text;
        this.type = type;
    }
}

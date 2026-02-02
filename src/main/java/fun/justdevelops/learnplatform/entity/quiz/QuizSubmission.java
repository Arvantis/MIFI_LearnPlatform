package fun.justdevelops.learnplatform.entity.quiz;

import fun.justdevelops.learnplatform.entity.AbstractEntity;
import fun.justdevelops.learnplatform.entity.person.Student;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_submissions")
public class QuizSubmission extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Getter
    @Column(name = "score")
    private Integer score;

    public QuizSubmission(Quiz quiz, Student student, Integer score) {
        this.quiz = quiz;
        this.student = student;
        this.score = score;
    }
}

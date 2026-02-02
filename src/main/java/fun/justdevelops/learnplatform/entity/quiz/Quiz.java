package fun.justdevelops.learnplatform.entity.quiz;

import fun.justdevelops.learnplatform.entity.AbstractEntity;
import fun.justdevelops.learnplatform.entity.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Getter
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "time_limit", nullable = false)
    private Integer timeLimit;

    @Getter
    @Setter
    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    @Setter
    private List<QuizSubmission> submissions = new ArrayList<>();

    public Quiz(Course course, String title, Integer timeLimit) {
        this.course = course;
        this.title = title;
        this.timeLimit = timeLimit;
    }


}

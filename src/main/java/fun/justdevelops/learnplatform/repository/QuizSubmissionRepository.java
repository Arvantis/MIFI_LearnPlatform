package fun.justdevelops.learnplatform.repository;

import fun.justdevelops.learnplatform.entity.quiz.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, UUID> {
    List<QuizSubmission> findByQuizId(UUID quizId);

    List<QuizSubmission> findByStudentId(UUID studentId);

    Optional<QuizSubmission> findByQuizIdAndStudentId(UUID quizId, UUID studentId);

    boolean existsByQuizIdAndStudentId(UUID quizId, UUID studentId);
}

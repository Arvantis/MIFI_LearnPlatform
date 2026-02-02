package fun.justdevelops.learnplatform.controller;

import fun.justdevelops.learnplatform.entity.quiz.AnswerOption;
import fun.justdevelops.learnplatform.entity.quiz.Question;
import fun.justdevelops.learnplatform.entity.quiz.Quiz;
import fun.justdevelops.learnplatform.entity.quiz.QuizSubmission;
import fun.justdevelops.learnplatform.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @RequestParam UUID courseId,
            @RequestParam String title,
            @RequestParam Integer timeLimit) {
        Quiz quiz = quizService.createQuiz(courseId, title, timeLimit);
        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<Question> addQuestion(
            @PathVariable UUID quizId,
            @RequestParam String text,
            @RequestParam String type) {
        Question question = quizService.addQuestionToQuiz(quizId, text, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<AnswerOption> addAnswerOption(
            @PathVariable UUID questionId,
            @RequestParam String text,
            @RequestParam boolean isCorrect) {
        AnswerOption option = quizService.addAnswerOption(questionId, text, isCorrect);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }

    @PostMapping("/{quizId}/take")
    public ResponseEntity<QuizSubmission> takeQuiz(
            @PathVariable UUID quizId,
            @RequestParam UUID studentId,
            @RequestBody Map<UUID, UUID> answers) {
        QuizSubmission submission = quizService.takeQuiz(quizId, studentId, answers);
        return ResponseEntity.status(HttpStatus.CREATED).body(submission);
    }

    @GetMapping("/{quizId}/submissions")
    public ResponseEntity<List<QuizSubmission>> getQuizSubmissions(@PathVariable UUID quizId) {
        List<QuizSubmission> submissions = quizService.getQuizSubmissions(quizId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/students/{studentId}/submissions")
    public ResponseEntity<List<QuizSubmission>> getStudentQuizSubmissions(@PathVariable UUID studentId) {
        List<QuizSubmission> submissions = quizService.getStudentQuizSubmissions(studentId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Quiz> getQuizByCourseId(@PathVariable UUID courseId) {
        Quiz quiz = quizService.getQuizByModuleId(courseId);
        return ResponseEntity.ok(quiz);
    }
}
package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.entity.quiz.AnswerOption;
import fun.justdevelops.learnplatform.entity.quiz.Question;
import fun.justdevelops.learnplatform.entity.quiz.Quiz;
import fun.justdevelops.learnplatform.entity.quiz.QuizSubmission;
import fun.justdevelops.learnplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final CourseRepository courseRepository;
    private final PersonRepository personRepository;


    public Quiz createQuiz(UUID courseId, String title, Integer timeLimit) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        Quiz quiz = new Quiz(course, title, timeLimit);
        return quizRepository.save(quiz);
    }

    public Question addQuestionToQuiz(UUID quizId, String text, String type) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));

        Question question = new Question(quiz, text, type);
        return questionRepository.save(question);
    }

    public AnswerOption addAnswerOption(UUID questionId, String text, boolean isCorrect) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Вопрос не найден"));

        AnswerOption option = new AnswerOption(question, text, isCorrect);
        return answerOptionRepository.save(option);
    }

    public QuizSubmission takeQuiz(UUID quizId, UUID studentId, Map<UUID, UUID> answers) {
        if (quizSubmissionRepository.existsByQuizIdAndStudentId(quizId, studentId)) {
            throw new RuntimeException("Студент уже прошел этот тест");
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));
        Student student = personRepository.findStudentEntityById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));

        int correctAnswers = 0;
        for (Question question : quiz.getQuestions()) {
            UUID selectedOptionId = answers.get(question.getId());
            if (selectedOptionId != null) {
                AnswerOption selectedOption = answerOptionRepository.findById(selectedOptionId)
                        .orElseThrow(() -> new RuntimeException("Вариант ответа не найден"));
                if (selectedOption.isCorrect()) {
                    correctAnswers++;
                }
            }
        }

        int score = !quiz.getQuestions().isEmpty() ?
                (correctAnswers * 100) / quiz.getQuestions().size() : 0;

        QuizSubmission submission = new QuizSubmission(quiz, student, score);
        return quizSubmissionRepository.save(submission);
    }

    public List<QuizSubmission> getQuizSubmissions(UUID quizId) {
        return quizSubmissionRepository.findByQuizId(quizId);
    }

    public List<QuizSubmission> getStudentQuizSubmissions(UUID studentId) {
        return quizSubmissionRepository.findByStudentId(studentId);
    }

    public Quiz getQuizByModuleId(UUID courseId) {
        return quizRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Тест не найден для этого модуля"));
    }
}

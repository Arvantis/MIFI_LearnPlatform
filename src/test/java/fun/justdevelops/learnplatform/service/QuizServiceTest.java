// QuizServiceTest.java
package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.entity.quiz.AnswerOption;
import fun.justdevelops.learnplatform.entity.quiz.Question;
import fun.justdevelops.learnplatform.entity.quiz.Quiz;
import fun.justdevelops.learnplatform.entity.quiz.QuizSubmission;
import fun.justdevelops.learnplatform.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerOptionRepository answerOptionRepository;

    @Mock
    private QuizSubmissionRepository quizSubmissionRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private QuizService quizService;

    private UUID quizId;
    private UUID courseId;
    private UUID studentId;
    private UUID questionId;
    private Course course;
    private Student student;
    private Quiz quiz;
    private Question question;
    private AnswerOption correctOption;
    private AnswerOption wrongOption;

    @BeforeEach
    void setUp() {
        quizId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        questionId = UUID.randomUUID();

        course = new Course();
        course.setId(courseId);

        student = new Student();
        student.setId(studentId);

        quiz = new Quiz(course, "Test Quiz", 30);
        quiz.setId(quizId);

        question = new Question(quiz, "Test Question", "multiple");
        question.setId(questionId);

        correctOption = new AnswerOption(question, "Correct", true);
        wrongOption = new AnswerOption(question, "Wrong", false);

        quiz.setQuestions(List.of(question));
        question.setOptions(List.of(correctOption, wrongOption));
    }

    @Test
    void createQuiz_ShouldReturnQuiz() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        Quiz result = quizService.createQuiz(courseId, "Test Quiz", 30);

        assertNotNull(result);
        assertEquals("Test Quiz", result.getTitle());
    }

    @Test
    void addQuestionToQuiz_ShouldReturnQuestion() {
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question result = quizService.addQuestionToQuiz(quizId, "Test Question", "multiple");

        assertNotNull(result);
        assertEquals("Test Question", result.getText());
    }

    @Test
    void addAnswerOption_ShouldReturnAnswerOption() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(answerOptionRepository.save(any(AnswerOption.class))).thenReturn(correctOption);

        AnswerOption result = quizService.addAnswerOption(questionId, "Correct", true);

        assertNotNull(result);
        assertTrue(result.isCorrect());
    }

    @Test
    void takeQuiz_WhenAlreadyTaken_ShouldThrowException() {
        when(quizSubmissionRepository.existsByQuizIdAndStudentId(quizId, studentId)).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                quizService.takeQuiz(quizId, studentId, Collections.emptyMap()));
    }

    @Test
    void getQuizByModuleId_ShouldReturnQuiz() {
        when(quizRepository.findByCourseId(courseId)).thenReturn(Optional.of(quiz));

        Quiz result = quizService.getQuizByModuleId(courseId);

        assertNotNull(result);
        assertEquals(quizId, result.getId());
    }
}
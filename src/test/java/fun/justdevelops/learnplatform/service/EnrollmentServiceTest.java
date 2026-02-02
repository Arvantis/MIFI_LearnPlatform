package fun.justdevelops.learnplatform.service;
import fun.justdevelops.learnplatform.dto.response.EnrollmentResponse;
import fun.justdevelops.learnplatform.dto.response.StudentResponse;
import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.Enrollment;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.enums.EnrollmentRole;
import fun.justdevelops.learnplatform.enums.EnrollmentStatus;
import fun.justdevelops.learnplatform.enums.StudentStatus;
import fun.justdevelops.learnplatform.exception.AppException;
import fun.justdevelops.learnplatform.mapper.EnrollmentMapper;
import fun.justdevelops.learnplatform.repository.CourseRepository;
import fun.justdevelops.learnplatform.repository.EnrollmentRepository;
import fun.justdevelops.learnplatform.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @Mock
    private PersonService personService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private UUID studentId;
    private UUID courseId;
    private UUID enrollmentId;
    private Student student;
    private Course course;
    private Enrollment enrollment;
    private StudentResponse studentResponse;
    private EnrollmentResponse enrollmentResponse;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        enrollmentId = UUID.randomUUID();

        student = new Student();
        student.setId(studentId);

        course = new Course();
        course.setId(courseId);

        enrollment = new Enrollment();
        enrollment.setId(enrollmentId);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        studentResponse = new StudentResponse();
        studentResponse.setId(studentId);
        studentResponse.setStatus(StudentStatus.ACTIVE);

        enrollmentResponse = new EnrollmentResponse();
        enrollmentResponse.setId(enrollmentId);
    }

    @Test
    void enrollStudent_ShouldCreateEnrollment() {
        when(personService.findStudentById(studentId)).thenReturn(studentResponse);
        when(personRepository.findStudentEntityById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(enrollmentResponse);

        EnrollmentResponse result = enrollmentService.enrollStudent(studentId, courseId, EnrollmentRole.STUDENT);

        assertNotNull(result);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void enrollStudent_WhenStudentNotActive_ShouldThrowException() {
        studentResponse.setStatus(StudentStatus.SUSPENDED);
        when(personService.findStudentById(studentId)).thenReturn(studentResponse);

        assertThrows(AppException.class, () ->
                enrollmentService.enrollStudent(studentId, courseId, EnrollmentRole.STUDENT));
    }

    @Test
    void completeEnrollment_ShouldUpdateStatusAndGrade() {
        Double finalGrade = 95.5;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));

        enrollmentService.completeEnrollment(enrollmentId, finalGrade);

        assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
        assertEquals(finalGrade, enrollment.getFinalGrade());
    }

    @Test
    void dropEnrollment_ShouldUpdateStatus() {
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));

        enrollmentService.dropEnrollment(enrollmentId);

        assertEquals(EnrollmentStatus.DROPPED, enrollment.getStatus());
    }
}
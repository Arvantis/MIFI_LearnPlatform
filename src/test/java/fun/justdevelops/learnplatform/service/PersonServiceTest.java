package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.dto.request.StudentRequest;
import fun.justdevelops.learnplatform.dto.request.TeacherRequest;
import fun.justdevelops.learnplatform.dto.response.StudentResponse;
import fun.justdevelops.learnplatform.dto.response.TeacherResponse;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.entity.person.Teacher;
import fun.justdevelops.learnplatform.enums.StudentStatus;
import fun.justdevelops.learnplatform.exception.AppException;
import fun.justdevelops.learnplatform.mapper.PersonMapper;
import fun.justdevelops.learnplatform.mapper.StudentMapper;
import fun.justdevelops.learnplatform.mapper.TeacherMapper;
import fun.justdevelops.learnplatform.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    private UUID studentId;
    private UUID teacherId;
    private StudentRequest studentRequest;
    private TeacherRequest teacherRequest;
    private Student student;
    private Teacher teacher;
    private StudentResponse studentResponse;
    private TeacherResponse teacherResponse;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        teacherId = UUID.randomUUID();

        studentRequest = new StudentRequest();
        studentRequest.setStudentNo("12345");

        teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("John");

        student = new Student();
        student.setId(studentId);
        student.setEmails(new HashSet<>());
        student.setPhones(new HashSet<>());

        teacher = new Teacher();
        teacher.setId(teacherId);

        studentResponse = new StudentResponse();
        studentResponse.setId(studentId);

        teacherResponse = new TeacherResponse();
        teacherResponse.setId(teacherId);
    }

    @Test
    void createStudent_ShouldReturnStudentResponse() {
        when(personRepository.findByStudentNo("12345")).thenReturn(Optional.empty());
        when(studentMapper.toEntity(studentRequest)).thenReturn(student);
        when(personRepository.save(student)).thenReturn(student);
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        StudentResponse result = personService.createStudent(studentRequest);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
    }

    @Test
    void createStudent_WhenStudentNoExists_ShouldThrowException() {
        when(personRepository.findByStudentNo("12345")).thenReturn(Optional.of(student));

        assertThrows(AppException.class, () -> personService.createStudent(studentRequest));
    }

    @Test
    void createTeacher_ShouldReturnTeacherResponse() {
        when(teacherMapper.toEntity(teacherRequest)).thenReturn(teacher);
        when(personRepository.save(teacher)).thenReturn(teacher);
        when(teacherMapper.toResponse(teacher)).thenReturn(teacherResponse);

        TeacherResponse result = personService.createTeacher(teacherRequest);

        assertNotNull(result);
        assertEquals(teacherId, result.getId());
    }

    @Test
    void addEmail_ShouldAddEmailToStudent() {
        String email = "test@example.com";
        when(personRepository.findStudentEntityById(studentId)).thenReturn(Optional.of(student));

        personService.addEmail(studentId, email);

        assertTrue(student.getEmails().contains(email));
    }

    @Test
    void removeEmail_ShouldRemoveEmailFromStudent() {
        String email = "test@example.com";
        student.getEmails().add(email);
        when(personRepository.findStudentEntityById(studentId)).thenReturn(Optional.of(student));

        personService.removeEmail(studentId, email);

        assertFalse(student.getEmails().contains(email));
    }

    @Test
    void changeStudentStatus_ShouldUpdateStatus() {
        when(personRepository.findStudentEntityById(studentId)).thenReturn(Optional.of(student));

        personService.changeStudentStatus(studentId, StudentStatus.SUSPENDED);

        assertEquals(StudentStatus.SUSPENDED, student.getStatus());
    }

    @Test
    void deleteStudent_ShouldCallRepositoryDelete() {
        personService.deleteStudent(studentId);

        verify(personRepository).deleteStudent(studentId);
    }
}
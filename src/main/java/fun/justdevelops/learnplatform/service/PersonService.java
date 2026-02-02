package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.dto.request.StudentRequest;
import fun.justdevelops.learnplatform.dto.request.TeacherRequest;
import fun.justdevelops.learnplatform.dto.response.PersonResponse;
import fun.justdevelops.learnplatform.dto.response.StudentResponse;
import fun.justdevelops.learnplatform.dto.response.TeacherResponse;
import fun.justdevelops.learnplatform.entity.person.Person;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.entity.person.Teacher;
import fun.justdevelops.learnplatform.enums.StudentStatus;
import fun.justdevelops.learnplatform.exception.AppException;
import fun.justdevelops.learnplatform.mapper.PersonMapper;
import fun.justdevelops.learnplatform.mapper.StudentMapper;
import fun.justdevelops.learnplatform.mapper.TeacherMapper;
import fun.justdevelops.learnplatform.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final PersonMapper personMapper;

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        personRepository.findByStudentNo(request.getStudentNo()).ifPresent(s -> {
            throw new AppException("Student with studentNo '" + request.getStudentNo() + "' already exists", 409);
        });

        Student student = studentMapper.toEntity(request);
        Student saved = personRepository.save(student);
        return studentMapper.toResponse(saved);
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        Teacher teacher = teacherMapper.toEntity(request);
        Teacher saved = personRepository.save(teacher);
        return teacherMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StudentResponse findStudentById(UUID id) {
        Student student = personRepository.findStudentEntityById(id).orElseThrow();
        return studentMapper.toResponse(student);
    }

    @Transactional(readOnly = true)
    public TeacherResponse findTeacherById(UUID id) {
        Teacher teacher = personRepository.findTeacherEntityById(id);
        return teacherMapper.toResponse(teacher);
    }

    @Transactional(readOnly = true)
    public PersonResponse findPersonById(UUID id) {
        Person person = personRepository.findById(id).orElseThrow();
        return personMapper.toResponse(person);
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> findAllPersons() {
        List<Person> persons = personRepository.findAll();
        return personMapper.toResponseList(persons);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAllStudents() {
        List<Student> students = personRepository.findAllStudents();
        return studentMapper.toResponseList(students);
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> findAllTeachers() {
        List<Teacher> teachers = personRepository.findAllTeachers();
        return teacherMapper.toResponseList(teachers);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findStudentsByStatus(StudentStatus status) {
        List<Student> students = personRepository.findByStatus(status);
        return studentMapper.toResponseList(students);
    }

    @Transactional
    public StudentResponse updateStudent(UUID id, StudentRequest request) {
        Student student = personRepository.findStudentEntityById(id).orElseThrow();
        studentMapper.updateEntityFromRequest(request, student);

        Student updated = personRepository.save(student);
        return studentMapper.toResponse(updated);
    }

    @Transactional
    public TeacherResponse updateTeacher(UUID id, TeacherRequest request) {
        Teacher teacher = personRepository.findTeacherEntityById(id);
        teacherMapper.updateEntityFromRequest(request, teacher);

        Teacher updated = personRepository.save(teacher);
        return teacherMapper.toResponse(updated);
    }

    @Transactional
    public void addEmail(UUID studentId, String email) {
        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        student.getEmails().add(email);
    }

    @Transactional
    public void removeEmail(UUID studentId, String email) {
        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        student.getEmails().remove(email);
    }

    @Transactional
    public void setEmails(UUID studentId, Set<String> emails) {
        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        student.getEmails().clear();
        student.getEmails().addAll(emails);
    }

    @Transactional
    public void addPhone(UUID studentId, String phone) {
        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        student.getPhones().add(phone);
    }

    @Transactional
    public void removePhone(UUID studentId, String phone) {
        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        student.getPhones().remove(phone);
    }

    @Transactional
    public void setPhones(UUID studentId, Set<String> phones) {
        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        student.getPhones().clear();
        student.getPhones().addAll(phones);
    }

    @Transactional
    public void changeStudentStatus(UUID studentId, StudentStatus status) {
        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        student.setStatus(status);
    }

    @Transactional
    public void deleteStudent(UUID id) {
        personRepository.deleteStudent(id);
    }

    @Transactional
    public void deleteTeacher(UUID id) {
        personRepository.deleteTeacher(id);
    }

    @Transactional
    public void deletePerson(UUID id) {
        personRepository.deleteById(id);
    }
}

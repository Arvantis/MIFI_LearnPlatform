package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.dto.request.EnrollmentRequest;
import fun.justdevelops.learnplatform.dto.response.CourseShortResponse;
import fun.justdevelops.learnplatform.dto.response.EnrollmentResponse;
import fun.justdevelops.learnplatform.dto.response.StudentResponse;
import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.Enrollment;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.enums.EnrollmentRole;
import fun.justdevelops.learnplatform.enums.EnrollmentStatus;
import fun.justdevelops.learnplatform.enums.StudentStatus;
import fun.justdevelops.learnplatform.exception.AppException;
import fun.justdevelops.learnplatform.exception.NotFoundException;
import fun.justdevelops.learnplatform.mapper.EnrollmentMapper;
import fun.justdevelops.learnplatform.repository.CourseRepository;
import fun.justdevelops.learnplatform.repository.EnrollmentRepository;
import fun.justdevelops.learnplatform.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final PersonRepository personRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final PersonService personService;
    private final CourseService courseService;

    @Transactional
    public EnrollmentResponse enrollStudent(UUID studentId, UUID courseId, EnrollmentRole role) {
        StudentResponse studentResponse = personService.findStudentById(studentId);

        if (studentResponse.getStatus() != StudentStatus.ACTIVE) {
            throw new AppException("Student is not active", 400);
        }

        courseService.findById(courseId);

        enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).ifPresent(e -> {
            throw new AppException("Student is already enrolled in this course", 409);
        });

        Student student = personRepository.findStudentEntityById(studentId).orElseThrow();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setRole(role);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(saved);
    }

    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        personService.findStudentById(request.getStudentId());
        courseService.findById(request.getCourseId());

        enrollmentRepository.findByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())
                .ifPresent(e -> {
                    throw new AppException("Enrollment already exists", 409);
                });

        Student student = personRepository.findStudentEntityById(request.getStudentId()).orElseThrow();
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + request.getCourseId()));

        Enrollment enrollment = enrollmentMapper.toEntity(request);
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse findById(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow();
        return enrollmentMapper.toResponse(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findAll() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStudentId(UUID studentId) {
        personService.findStudentById(studentId);

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByCourseId(UUID courseId) {
        courseService.findById(courseId);

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<CourseShortResponse> getStudentCourses(UUID studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        List<UUID> courseIds = enrollments.stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toList());

        return courseIds.stream()
                .map(courseService::findById)
                .map(courseResponse -> {
                    CourseShortResponse shortResponse = new CourseShortResponse();
                    shortResponse.setId(courseResponse.getId());
                    shortResponse.setCode(courseResponse.getCode());
                    shortResponse.setTitle(courseResponse.getTitle());
                    shortResponse.setCredits(courseResponse.getCredits());
                    return shortResponse;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getCourseStudents(UUID courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        List<UUID> studentIds = enrollments.stream()
                .map(e -> e.getStudent().getId())
                .collect(Collectors.toList());

        return studentIds.stream()
                .map(personService::findStudentById)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentResponse update(UUID id, EnrollmentRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow();
        enrollmentMapper.updateEntityFromRequest(request, enrollment);

        Enrollment updated = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(updated);
    }

    @Transactional
    public void completeEnrollment(UUID enrollmentId, Double finalGrade) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setFinalGrade(finalGrade);
    }

    @Transactional
    public void dropEnrollment(UUID enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        enrollment.setStatus(EnrollmentStatus.DROPPED);
    }

    @Transactional
    public void delete(UUID id) {
        enrollmentRepository.deleteById(id);
    }
}

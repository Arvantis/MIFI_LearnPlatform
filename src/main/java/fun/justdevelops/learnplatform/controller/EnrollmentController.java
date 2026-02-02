package fun.justdevelops.learnplatform.controller;

import fun.justdevelops.learnplatform.dto.request.EnrollmentRequest;
import fun.justdevelops.learnplatform.dto.response.CourseShortResponse;
import fun.justdevelops.learnplatform.dto.response.EnrollmentResponse;
import fun.justdevelops.learnplatform.dto.response.StudentResponse;
import fun.justdevelops.learnplatform.enums.EnrollmentRole;
import fun.justdevelops.learnplatform.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @RequestParam UUID studentId,
            @RequestParam UUID courseId,
            @RequestParam EnrollmentRole role) {
        EnrollmentResponse response = enrollmentService.enrollStudent(studentId, courseId, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> create(@RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> findById(@PathVariable UUID id) {
        EnrollmentResponse response = enrollmentService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> findAll() {
        List<EnrollmentResponse> responses = enrollmentService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> findByStudentId(@PathVariable UUID studentId) {
        List<EnrollmentResponse> responses = enrollmentService.findByStudentId(studentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> findByCourseId(@PathVariable UUID courseId) {
        List<EnrollmentResponse> responses = enrollmentService.findByCourseId(courseId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<List<CourseShortResponse>> getStudentCourses(@PathVariable UUID studentId) {
        List<CourseShortResponse> responses = enrollmentService.getStudentCourses(studentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<List<StudentResponse>> getCourseStudents(@PathVariable UUID courseId) {
        List<StudentResponse> responses = enrollmentService.getCourseStudents(courseId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> update(
            @PathVariable UUID id,
            @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{enrollmentId}/complete")
    public ResponseEntity<Void> completeEnrollment(
            @PathVariable UUID enrollmentId,
            @RequestParam Double finalGrade) {
        enrollmentService.completeEnrollment(enrollmentId, finalGrade);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{enrollmentId}/drop")
    public ResponseEntity<Void> dropEnrollment(@PathVariable UUID enrollmentId) {
        enrollmentService.dropEnrollment(enrollmentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
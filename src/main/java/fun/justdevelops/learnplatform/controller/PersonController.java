package fun.justdevelops.learnplatform.controller;

import fun.justdevelops.learnplatform.dto.request.StudentRequest;
import fun.justdevelops.learnplatform.dto.request.TeacherRequest;
import fun.justdevelops.learnplatform.dto.response.PersonResponse;
import fun.justdevelops.learnplatform.dto.response.StudentResponse;
import fun.justdevelops.learnplatform.dto.response.TeacherResponse;
import fun.justdevelops.learnplatform.enums.StudentStatus;
import fun.justdevelops.learnplatform.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping("/students")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest request) {
        StudentResponse response = personService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/teachers")
    public ResponseEntity<TeacherResponse> createTeacher(@RequestBody TeacherRequest request) {
        TeacherResponse response = personService.createTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> findStudentById(@PathVariable UUID id) {
        StudentResponse response = personService.findStudentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<TeacherResponse> findTeacherById(@PathVariable UUID id) {
        TeacherResponse response = personService.findTeacherById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findPersonById(@PathVariable UUID id) {
        PersonResponse response = personService.findPersonById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> findAllPersons() {
        List<PersonResponse> responses = personService.findAllPersons();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> findAllStudents() {
        List<StudentResponse> responses = personService.findAllStudents();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherResponse>> findAllTeachers() {
        List<TeacherResponse> responses = personService.findAllTeachers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/students/status/{status}")
    public ResponseEntity<List<StudentResponse>> findStudentsByStatus(@PathVariable StudentStatus status) {
        List<StudentResponse> responses = personService.findStudentsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable UUID id,
            @RequestBody StudentRequest request) {
        StudentResponse response = personService.updateStudent(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/teachers/{id}")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable UUID id,
            @RequestBody TeacherRequest request) {
        TeacherResponse response = personService.updateTeacher(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/students/{studentId}/emails")
    public ResponseEntity<Void> addEmail(
            @PathVariable UUID studentId,
            @RequestParam String email) {
        personService.addEmail(studentId, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/students/{studentId}/emails")
    public ResponseEntity<Void> removeEmail(
            @PathVariable UUID studentId,
            @RequestParam String email) {
        personService.removeEmail(studentId, email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/students/{studentId}/emails")
    public ResponseEntity<Void> setEmails(
            @PathVariable UUID studentId,
            @RequestBody Set<String> emails) {
        personService.setEmails(studentId, emails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/students/{studentId}/phones")
    public ResponseEntity<Void> addPhone(
            @PathVariable UUID studentId,
            @RequestParam String phone) {
        personService.addPhone(studentId, phone);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/students/{studentId}/phones")
    public ResponseEntity<Void> removePhone(
            @PathVariable UUID studentId,
            @RequestParam String phone) {
        personService.removePhone(studentId, phone);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/students/{studentId}/phones")
    public ResponseEntity<Void> setPhones(
            @PathVariable UUID studentId,
            @RequestBody Set<String> phones) {
        personService.setPhones(studentId, phones);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/students/{studentId}/status")
    public ResponseEntity<Void> changeStudentStatus(
            @PathVariable UUID studentId,
            @RequestParam StudentStatus status) {
        personService.changeStudentStatus(studentId, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        personService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable UUID id) {
        personService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
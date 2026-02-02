package fun.justdevelops.learnplatform.controller;

import fun.justdevelops.learnplatform.dto.request.LessonRequest;
import fun.justdevelops.learnplatform.dto.response.LessonResponse;
import fun.justdevelops.learnplatform.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonResponse> create(@RequestBody LessonRequest request) {
        LessonResponse response = lessonService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> findById(@PathVariable UUID id) {
        LessonResponse response = lessonService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> findAll() {
        List<LessonResponse> responses = lessonService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonResponse>> findByCourseId(@PathVariable UUID courseId) {
        List<LessonResponse> responses = lessonService.findByCourseId(courseId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> update(
            @PathVariable UUID id,
            @RequestBody LessonRequest request) {
        LessonResponse response = lessonService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{lessonId}/resources")
    public ResponseEntity<Void> addResource(
            @PathVariable UUID lessonId,
            @RequestParam String resourceUrl) {
        lessonService.addResource(lessonId, resourceUrl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lessonId}/resources/position")
    public ResponseEntity<Void> addResourceAtPosition(
            @PathVariable UUID lessonId,
            @RequestParam String resourceUrl,
            @RequestParam int position) {
        lessonService.addResourceAtPosition(lessonId, resourceUrl, position);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lessonId}/resources/position")
    public ResponseEntity<Void> removeResourceAtPosition(
            @PathVariable UUID lessonId,
            @RequestParam int position) {
        lessonService.removeResourceAtPosition(lessonId, position);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{lessonId}/resources/move")
    public ResponseEntity<Void> moveResource(
            @PathVariable UUID lessonId,
            @RequestParam int fromPosition,
            @RequestParam int toPosition) {
        lessonService.moveResource(lessonId, fromPosition, toPosition);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{lessonId}/syllabus")
    public ResponseEntity<Void> updateSyllabus(
            @PathVariable UUID lessonId,
            @RequestParam String syllabus) {
        lessonService.updateSyllabus(lessonId, syllabus);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
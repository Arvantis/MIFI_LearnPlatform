package fun.justdevelops.learnplatform.controller;

import fun.justdevelops.learnplatform.dto.request.CourseRequest;
import fun.justdevelops.learnplatform.dto.request.LessonRequest;
import fun.justdevelops.learnplatform.dto.response.CourseResponse;
import fun.justdevelops.learnplatform.dto.response.CourseShortResponse;
import fun.justdevelops.learnplatform.dto.response.LessonResponse;
import fun.justdevelops.learnplatform.dto.response.TagShortResponse;
import fun.justdevelops.learnplatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody CourseRequest request) {
        CourseResponse response = courseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/with-tags")
    public ResponseEntity<CourseResponse> createWithTags(
            @RequestBody CourseRequest request,
            @RequestParam List<UUID> tagIds) {
        CourseResponse response = courseService.createWithTags(request, tagIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> findById(@PathVariable UUID id) {
        CourseResponse response = courseService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> findAll() {
        List<CourseResponse> responses = courseService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/short")
    public ResponseEntity<List<CourseShortResponse>> findAllShort() {
        List<CourseShortResponse> responses = courseService.findAllShort();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @PathVariable UUID id,
            @RequestBody CourseRequest request) {
        CourseResponse response = courseService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/keywords")
    public ResponseEntity<Void> addKeyword(
            @PathVariable UUID courseId,
            @RequestParam String keyword) {
        courseService.addKeyword(courseId, keyword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/keywords")
    public ResponseEntity<Void> removeKeyword(
            @PathVariable UUID courseId,
            @RequestParam String keyword) {
        courseService.removeKeyword(courseId, keyword);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{courseId}/keywords")
    public ResponseEntity<Void> setKeywords(
            @PathVariable UUID courseId,
            @RequestBody List<String> keywords) {
        courseService.setKeywords(courseId, Set.copyOf(keywords));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/tags/{tagId}")
    public ResponseEntity<Void> addTag(
            @PathVariable UUID courseId,
            @PathVariable UUID tagId) {
        courseService.addTagToCourse(courseId, tagId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/tags/by-slug")
    public ResponseEntity<Void> addTagBySlug(
            @PathVariable UUID courseId,
            @RequestParam String slug) {
        courseService.addTagBySLug(courseId, slug);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/tags/{tagId}")
    public ResponseEntity<Void> removeTag(
            @PathVariable UUID courseId,
            @PathVariable UUID tagId) {
        courseService.removeTagFromCourse(courseId, tagId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseId}/tags")
    public ResponseEntity<List<TagShortResponse>> getCourseTags(@PathVariable UUID courseId) {
        List<TagShortResponse> responses = courseService.getCourseTagsShort(courseId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<LessonResponse> addLesson(
            @PathVariable UUID courseId,
            @RequestBody LessonRequest lessonRequest) {
        LessonResponse response = courseService.addLessonToCourse(courseId, lessonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<List<LessonResponse>> getCourseLessons(@PathVariable UUID courseId) {
        List<LessonResponse> responses = courseService.getCourseLessons(courseId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
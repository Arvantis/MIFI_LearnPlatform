package fun.justdevelops.learnplatform.controller;

import fun.justdevelops.learnplatform.dto.request.TagRequest;
import fun.justdevelops.learnplatform.dto.response.CourseShortResponse;
import fun.justdevelops.learnplatform.dto.response.TagResponse;
import fun.justdevelops.learnplatform.dto.response.TagShortResponse;
import fun.justdevelops.learnplatform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> create(@RequestBody TagRequest request) {
        TagResponse response = tagService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> findById(@PathVariable UUID id) {
        TagResponse response = tagService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<TagResponse> findBySlug(@PathVariable String slug) {
        TagResponse response = tagService.findBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> findAll() {
        List<TagResponse> responses = tagService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/short")
    public ResponseEntity<List<TagShortResponse>> findAllShort() {
        List<TagShortResponse> responses = tagService.findAllShort();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{tagId}/courses")
    public ResponseEntity<List<CourseShortResponse>> getTagCourses(@PathVariable UUID tagId) {
        List<CourseShortResponse> responses = tagService.getTagCourses(tagId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> update(
            @PathVariable UUID id,
            @RequestBody TagRequest request) {
        TagResponse response = tagService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<Void> forceDelete(@PathVariable UUID id) {
        tagService.forceDelete(id);
        return ResponseEntity.noContent().build();
    }
}
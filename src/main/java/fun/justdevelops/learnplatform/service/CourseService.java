package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.dto.request.CourseRequest;
import fun.justdevelops.learnplatform.dto.request.LessonRequest;
import fun.justdevelops.learnplatform.dto.response.CourseResponse;
import fun.justdevelops.learnplatform.dto.response.CourseShortResponse;
import fun.justdevelops.learnplatform.dto.response.LessonResponse;
import fun.justdevelops.learnplatform.dto.response.TagShortResponse;
import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.Tag;
import fun.justdevelops.learnplatform.entity.person.Teacher;
import fun.justdevelops.learnplatform.exception.NotFoundException;
import fun.justdevelops.learnplatform.mapper.CourseMapper;
import fun.justdevelops.learnplatform.mapper.TagMapper;
import fun.justdevelops.learnplatform.repository.CourseRepository;
import fun.justdevelops.learnplatform.repository.PersonRepository;
import fun.justdevelops.learnplatform.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final PersonRepository personRepository;
    private final CourseMapper courseMapper;
    private final LessonService lessonService;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional
    public CourseResponse create(CourseRequest request) {
        Teacher author = personRepository.findTeacherEntityById(request.getAuthorId());

        Course course = courseMapper.toEntity(request);
        course.setAuthor(author);

        Course saved = courseRepository.save(course);
        return courseMapper.toResponse(saved);
    }

    @Transactional
    public CourseResponse createWithTags(CourseRequest request, List<UUID> tagIds) {
        CourseResponse courseResponse = create(request);

        for (UUID tagId : tagIds) {
            addTagToCourse(courseResponse.getId(), tagId);
        }

        return findById(courseResponse.getId());
    }

    @Transactional(readOnly = true)
    public CourseResponse findById(UUID id) {
        Course course = courseRepository.findById(id).orElse(null);
        return courseMapper.toResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> findAll() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toResponseList(courses);
    }

    @Transactional(readOnly = true)
    public List<CourseShortResponse> findAllShort() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toShortResponseList(courses);
    }

    @Transactional
    public CourseResponse update(UUID id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + id));

        if (request.getAuthorId() != null && !request.getAuthorId().equals(course.getAuthor().getId())) {
            Teacher newAuthor = personRepository.findTeacherEntityById(request.getAuthorId());
            course.setAuthor(newAuthor);
        }

        courseMapper.updateEntityFromRequest(request, course);

        Course updated = courseRepository.save(course);
        return courseMapper.toResponse(updated);
    }

    @Transactional
    public void addKeyword(UUID courseId, String keyword) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
        course.getKeywords().add(keyword);
    }

    @Transactional
    public void removeKeyword(UUID courseId, String keyword) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
        course.getKeywords().remove(keyword);
    }

    @Transactional
    public void setKeywords(UUID courseId, Set<String> keywords) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
        course.getKeywords().clear();
        course.getKeywords().addAll(keywords);
    }

    @Transactional
    public void addTagToCourse(UUID courseId, UUID tagId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
        Tag tag = new Tag();
        tag.setId(tagId);
        course.getTags().add(tag);
    }

    @Transactional
    public void addTagBySLug(UUID courseId, String slug) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
        Tag tag = new Tag();
        tag.setSlug(slug);
        course.getTags().add(tag);
    }

    @Transactional
    public void removeTagFromCourse(UUID courseId, UUID tagId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
        course.getTags().removeIf(tag -> tag.getId().equals(tagId));
    }

    @Transactional(readOnly = true)
    public List<TagShortResponse> getCourseTagsShort(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
        List<Tag> tags = course.getTags().stream().toList();
        return tagMapper.toShortResponseList(tags);
    }

    @Transactional
    public LessonResponse addLessonToCourse(UUID courseId, LessonRequest lessonRequest) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));

        lessonRequest.setCourseId(courseId);
        return lessonService.create(lessonRequest);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> getCourseLessons(UUID courseId) {
        return lessonService.findByCourseId(courseId);
    }

    @Transactional
    public void delete(UUID id) {
        courseRepository.deleteById(id);
    }
}

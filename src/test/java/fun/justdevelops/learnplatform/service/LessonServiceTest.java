package fun.justdevelops.learnplatform.service;
import fun.justdevelops.learnplatform.dto.request.LessonRequest;
import fun.justdevelops.learnplatform.dto.response.LessonResponse;
import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.Lesson;
import fun.justdevelops.learnplatform.mapper.LessonMapper;
import fun.justdevelops.learnplatform.repository.CourseRepository;
import fun.justdevelops.learnplatform.repository.LessonRepository;
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
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonService lessonService;

    private UUID lessonId;
    private UUID courseId;
    private LessonRequest lessonRequest;
    private Lesson lesson;
    private Course course;
    private LessonResponse lessonResponse;

    @BeforeEach
    void setUp() {
        lessonId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        lessonRequest = new LessonRequest();
        lessonRequest.setCourseId(courseId);
        lessonRequest.setTitle("Test Lesson");

        course = new Course();
        course.setId(courseId);
        course.setLessons(new ArrayList<>());

        lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setCourse(course);
        lesson.setResources(new ArrayList<>());

        lessonResponse = new LessonResponse();
        lessonResponse.setId(lessonId);
        lessonResponse.setTitle("Test Lesson");
    }

    @Test
    void create_ShouldReturnLessonResponse() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonMapper.toEntity(lessonRequest)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toResponse(lesson)).thenReturn(lessonResponse);

        LessonResponse result = lessonService.create(lessonRequest);

        assertNotNull(result);
        assertEquals(lessonId, result.getId());
        assertTrue(course.getLessons().contains(lesson));
    }

    @Test
    void addResource_ShouldAddResource() {
        String resourceUrl = "http://example.com/resource";
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        lessonService.addResource(lessonId, resourceUrl);

        assertTrue(lesson.getResources().contains(resourceUrl));
    }

    @Test
    void addResourceAtPosition_ShouldAddResourceAtSpecifiedPosition() {
        String resourceUrl = "http://example.com/resource";
        int position = 0;
        lesson.getResources().add("existing resource");
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        lessonService.addResourceAtPosition(lessonId, resourceUrl, position);

        assertEquals(resourceUrl, lesson.getResources().get(position));
    }

    @Test
    void removeResourceAtPosition_ShouldRemoveResource() {
        String resource = "resource to remove";
        lesson.getResources().add(resource);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        lessonService.removeResourceAtPosition(lessonId, 0);

        assertFalse(lesson.getResources().contains(resource));
    }

    @Test
    void updateSyllabus_ShouldUpdateSyllabus() {
        String syllabus = "New syllabus content";
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        lessonService.updateSyllabus(lessonId, syllabus);

        assertEquals(syllabus, lesson.getSyllabus());
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        lessonService.delete(lessonId);

        verify(lessonRepository).deleteById(lessonId);
    }
}
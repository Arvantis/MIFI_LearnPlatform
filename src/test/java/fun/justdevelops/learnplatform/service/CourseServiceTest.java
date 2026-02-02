package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.dto.request.CourseRequest;
import fun.justdevelops.learnplatform.dto.response.CourseResponse;
import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.person.Teacher;
import fun.justdevelops.learnplatform.exception.NotFoundException;
import fun.justdevelops.learnplatform.mapper.CourseMapper;
import fun.justdevelops.learnplatform.mapper.TagMapper;
import fun.justdevelops.learnplatform.repository.CourseRepository;
import fun.justdevelops.learnplatform.repository.PersonRepository;
import fun.justdevelops.learnplatform.repository.TagRepository;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private LessonService lessonService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private CourseService courseService;

    private UUID courseId;
    private UUID teacherId;
    private CourseRequest courseRequest;
    private Course course;
    private Teacher teacher;
    private CourseResponse courseResponse;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        teacherId = UUID.randomUUID();

        courseRequest = new CourseRequest();
        courseRequest.setAuthorId(teacherId);
        courseRequest.setTitle("Test Course");

        teacher = new Teacher();
        teacher.setId(teacherId);

        course = new Course();
        course.setId(courseId);
        course.setAuthor(teacher);

        courseResponse = new CourseResponse();
        courseResponse.setId(courseId);
        courseResponse.setTitle("Test Course");
    }

    @Test
    void create_ShouldReturnCourseResponse() {
        when(personRepository.findTeacherEntityById(teacherId)).thenReturn(teacher);
        when(courseMapper.toEntity(courseRequest)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toResponse(course)).thenReturn(courseResponse);

        CourseResponse result = courseService.create(courseRequest);

        assertNotNull(result);
        assertEquals(courseId, result.getId());
        verify(courseRepository).save(course);
    }

    @Test
    void findById_WhenCourseExists_ShouldReturnCourseResponse() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(courseResponse);

        CourseResponse result = courseService.findById(courseId);

        assertNotNull(result);
        assertEquals(courseId, result.getId());
    }

    @Test
    void findById_WhenCourseNotExists_ShouldReturnNull() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        CourseResponse result = courseService.findById(courseId);

        assertNull(result);
    }

    @Test
    void update_WhenCourseExists_ShouldReturnUpdatedCourse() {
        CourseRequest updateRequest = new CourseRequest();
        updateRequest.setAuthorId(teacherId);
        updateRequest.setTitle("Updated Course");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toResponse(course)).thenReturn(courseResponse);

        CourseResponse result = courseService.update(courseId, updateRequest);

        assertNotNull(result);
        verify(courseMapper).updateEntityFromRequest(updateRequest, course);
    }

    @Test
    void update_WhenCourseNotExists_ShouldThrowNotFoundException() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                courseService.update(courseId, courseRequest));
    }

    @Test
    void addKeyword_ShouldAddKeywordToCourse() {
        String keyword = "programming";
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        courseService.addKeyword(courseId, keyword);

        assertTrue(course.getKeywords().contains(keyword));
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        courseService.delete(courseId);

        verify(courseRepository).deleteById(courseId);
    }
}
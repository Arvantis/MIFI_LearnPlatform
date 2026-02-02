
package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.dto.request.TagRequest;
import fun.justdevelops.learnplatform.dto.response.TagResponse;
import fun.justdevelops.learnplatform.entity.Tag;
import fun.justdevelops.learnplatform.exception.AppException;
import fun.justdevelops.learnplatform.mapper.TagMapper;
import fun.justdevelops.learnplatform.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private TagService tagService;

    private UUID tagId;
    private TagRequest tagRequest;
    private Tag tag;
    private TagResponse tagResponse;

    @BeforeEach
    void setUp() {
        tagId = UUID.randomUUID();

        tagRequest = new TagRequest();
        tagRequest.setSlug("test-tag");
        tagRequest.setName("Test Tag");

        tag = new Tag();
        tag.setId(tagId);
        tag.setSlug("test-tag");
        tag.setName("Test Tag");

        tagResponse = new TagResponse();
        tagResponse.setId(tagId);
        tagResponse.setSlug("test-tag");
    }

    @Test
    void create_ShouldReturnTagResponse() {
        when(tagRepository.findBySlug("test-tag")).thenReturn(Optional.empty());
        when(tagMapper.toEntity(tagRequest)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toResponse(tag)).thenReturn(tagResponse);

        TagResponse result = tagService.create(tagRequest);

        assertNotNull(result);
        assertEquals("test-tag", result.getSlug());
    }

    @Test
    void create_WhenSlugExists_ShouldThrowException() {
        when(tagRepository.findBySlug("test-tag")).thenReturn(Optional.of(tag));

        assertThrows(AppException.class, () -> tagService.create(tagRequest));
    }

    @Test
    void findBySlug_WhenTagExists_ShouldReturnTagResponse() {
        when(tagRepository.findBySlug("test-tag")).thenReturn(Optional.of(tag));
        when(tagMapper.toResponse(tag)).thenReturn(tagResponse);

        TagResponse result = tagService.findBySlug("test-tag");

        assertNotNull(result);
        assertEquals("test-tag", result.getSlug());
    }

    @Test
    void findBySlug_WhenTagNotExists_ShouldThrowException() {
        when(tagRepository.findBySlug("test-tag")).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> tagService.findBySlug("test-tag"));
    }

    @Test
    void delete_WhenNoCourses_ShouldDeleteTag() {
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        tagService.delete(tagId);

        verify(tagRepository).deleteById(tagId);
    }

    @Test
    void delete_WhenHasCourses_ShouldThrowException() {
        tag.getCourses().add(new fun.justdevelops.learnplatform.entity.Course());
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        assertThrows(AppException.class, () -> tagService.delete(tagId));
    }

    @Test
    void forceDelete_ShouldClearCoursesAndDelete() {
        fun.justdevelops.learnplatform.entity.Course course = new fun.justdevelops.learnplatform.entity.Course();
        tag.getCourses().add(course);
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        tagService.forceDelete(tagId);

        assertTrue(tag.getCourses().isEmpty());
        verify(tagRepository).deleteById(tagId);
    }
}
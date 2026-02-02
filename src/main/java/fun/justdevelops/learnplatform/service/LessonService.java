package fun.justdevelops.learnplatform.service;

import fun.justdevelops.learnplatform.dto.request.LessonRequest;
import fun.justdevelops.learnplatform.dto.response.LessonResponse;
import fun.justdevelops.learnplatform.entity.Course;
import fun.justdevelops.learnplatform.entity.Lesson;
import fun.justdevelops.learnplatform.mapper.LessonMapper;
import fun.justdevelops.learnplatform.repository.CourseRepository;
import fun.justdevelops.learnplatform.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Transactional
    public LessonResponse create(LessonRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow();

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);

        course.getLessons().add(lesson);

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public LessonResponse findById(UUID id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        return lessonMapper.toResponse(lesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessonMapper.toResponseList(lessons);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> findByCourseId(UUID courseId) {
        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
        return lessonMapper.toResponseList(lessons);
    }

    @Transactional
    public LessonResponse update(UUID id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        lessonMapper.updateEntityFromRequest(request, lesson);

        Lesson updated = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updated);
    }

    @Transactional
    public void addResource(UUID lessonId, String resourceUrl) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.getResources().add(resourceUrl);
    }

    @Transactional
    public void addResourceAtPosition(UUID lessonId, String resourceUrl, int position) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        if (position < 0 || position > lesson.getResources().size()) {
            lesson.getResources().add(resourceUrl);
        } else {
            lesson.getResources().add(position, resourceUrl);
        }
    }

    @Transactional
    public void removeResourceAtPosition(UUID lessonId, int position) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        if (position >= 0 && position < lesson.getResources().size()) {
            lesson.getResources().remove(position);
        }
    }

    @Transactional
    public void moveResource(UUID lessonId, int fromPosition, int toPosition) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        List<String> resources = lesson.getResources();

        if (fromPosition >= 0 && fromPosition < resources.size() &&
            toPosition >= 0 && toPosition < resources.size()) {
            String resource = resources.remove(fromPosition);
            resources.add(toPosition, resource);
        }
    }

    @Transactional
    public void updateSyllabus(UUID lessonId, String syllabus) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setSyllabus(syllabus);
    }

    @Transactional
    public void delete(UUID id) {
        lessonRepository.deleteById(id);
    }
}

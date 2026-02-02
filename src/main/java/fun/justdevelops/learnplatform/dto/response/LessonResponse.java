package fun.justdevelops.learnplatform.dto.response;

import fun.justdevelops.learnplatform.dto.nested.CourseInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class LessonResponse {

    private UUID id;
    private String title;
    private String syllabus;
    private List<String> resources = new ArrayList<>();
    private CourseInfo course;
    private LocalDateTime createdAt;
}

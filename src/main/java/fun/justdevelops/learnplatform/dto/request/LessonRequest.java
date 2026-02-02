package fun.justdevelops.learnplatform.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class LessonRequest {

    private String title;
    private String syllabus;
    private List<String> resources = new ArrayList<>();
    private UUID courseId;
    private UUID primaryResourceId;
}

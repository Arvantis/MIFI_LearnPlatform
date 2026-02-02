package fun.justdevelops.learnplatform.dto.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class CourseRequest {

    private String code;
    private String title;
    private String description;
    private Integer credits;
    private Set<String> keywords = new HashSet<>();
    private UUID authorId;
    private UUID featuredResourceId;
}

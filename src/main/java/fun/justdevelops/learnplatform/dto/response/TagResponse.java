package fun.justdevelops.learnplatform.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TagResponse {

    private UUID id;
    private String name;
    private String slug;
    private LocalDateTime createdAt;
}

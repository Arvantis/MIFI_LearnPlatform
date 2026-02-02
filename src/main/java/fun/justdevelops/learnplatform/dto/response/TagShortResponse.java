package fun.justdevelops.learnplatform.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class TagShortResponse {

    private UUID id;
    private String name;
    private String slug;
}

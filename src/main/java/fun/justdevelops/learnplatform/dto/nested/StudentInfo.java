package fun.justdevelops.learnplatform.dto.nested;

import lombok.Data;

import java.util.UUID;

@Data
public class StudentInfo {

    private UUID id;
    private String firstName;
    private String lastName;
    private String studentNo;
}

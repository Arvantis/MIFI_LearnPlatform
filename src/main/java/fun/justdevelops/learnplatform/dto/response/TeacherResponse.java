package fun.justdevelops.learnplatform.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TeacherResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String externalRef;
    private LocalDate birthDate;
    private String academicTitle;
    private LocalDateTime createdAt;
}

package fun.justdevelops.learnplatform.dto.request;

import fun.justdevelops.learnplatform.enums.EnrollmentRole;
import fun.justdevelops.learnplatform.enums.EnrollmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EnrollmentRequest {

    private EnrollmentRole role;
    private LocalDateTime enrolledAt;
    private EnrollmentStatus status;
    private Double finalGrade;
    private UUID studentId;
    private UUID courseId;
}

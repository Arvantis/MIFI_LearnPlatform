package fun.justdevelops.learnplatform.dto.response;

import fun.justdevelops.learnplatform.dto.nested.CourseInfo;
import fun.justdevelops.learnplatform.dto.nested.StudentInfo;
import fun.justdevelops.learnplatform.enums.EnrollmentRole;
import fun.justdevelops.learnplatform.enums.EnrollmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EnrollmentResponse {

    private UUID id;
    private EnrollmentRole role;
    private LocalDateTime enrolledAt;
    private EnrollmentStatus status;
    private Double finalGrade;
    private StudentInfo student;
    private CourseInfo course;
    private LocalDateTime createdAt;
}

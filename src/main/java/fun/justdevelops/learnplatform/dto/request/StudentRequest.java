package fun.justdevelops.learnplatform.dto.request;

import fun.justdevelops.learnplatform.enums.StudentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class StudentRequest {

    private String firstName;
    private String lastName;
    private String externalRef;
    private LocalDate birthDate;
    private String studentNo;
    private StudentStatus status;
    private Set<String> emails = new HashSet<>();
    private Set<String> phones = new HashSet<>();
}

package fun.justdevelops.learnplatform.mapper;

import fun.justdevelops.learnplatform.dto.response.PersonResponse;
import fun.justdevelops.learnplatform.entity.person.Person;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.entity.person.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {

    @Mapping(target = "type", expression = "java(getPersonType(person))")
    PersonResponse toResponse(Person person);

    List<PersonResponse> toResponseList(List<Person> persons);

    default String getPersonType(Person person) {
        if (person instanceof Student) {
            return "STUDENT";
        } else if (person instanceof Teacher) {
            return "TEACHER";
        }
        return "UNKNOWN";
    }
}

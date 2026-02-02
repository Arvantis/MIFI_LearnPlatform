package fun.justdevelops.learnplatform.mapper;

import fun.justdevelops.learnplatform.dto.request.TeacherRequest;
import fun.justdevelops.learnplatform.dto.response.TeacherResponse;
import fun.justdevelops.learnplatform.entity.person.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {

    Teacher toEntity(TeacherRequest request);

    TeacherResponse toResponse(Teacher teacher);

    List<TeacherResponse> toResponseList(List<Teacher> teachers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(TeacherRequest request, @MappingTarget Teacher teacher);
}

package fun.justdevelops.learnplatform.mapper;

import fun.justdevelops.learnplatform.dto.request.TagRequest;
import fun.justdevelops.learnplatform.dto.response.TagResponse;
import fun.justdevelops.learnplatform.dto.response.TagShortResponse;
import fun.justdevelops.learnplatform.entity.Tag;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "courses", ignore = true)
    Tag toEntity(TagRequest request);

    TagResponse toResponse(Tag tag);

    TagShortResponse toShortResponse(Tag tag);

    List<TagResponse> toResponseList(List<Tag> tags);

    List<TagShortResponse> toShortResponseList(List<Tag> tags);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateEntityFromRequest(TagRequest request, @MappingTarget Tag tag);

    @AfterMapping
    default void afterToEntity(@MappingTarget Tag tag, TagRequest request) {
        if (tag.getSlug() == null || tag.getSlug().isEmpty()) {
            tag.setSlug(request.getName().toLowerCase().replaceAll("\\s+", "-"));
        }
    }
}

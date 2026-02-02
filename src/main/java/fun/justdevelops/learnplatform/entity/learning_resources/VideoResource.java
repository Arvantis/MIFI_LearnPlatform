package fun.justdevelops.learnplatform.entity.learning_resources;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("VIDEO")
public class VideoResource extends LearningResource {

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "duration_sec")
    private Integer durationSec;

    @Column(name = "transcript_url", length = 500)
    private String transcriptUrl;
}

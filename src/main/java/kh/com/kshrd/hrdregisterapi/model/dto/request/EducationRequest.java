package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.com.kshrd.hrdregisterapi.model.entity.Education;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationRequest {

    @NotNull
    @NotBlank
    private String levelOfEducation;

    public Education toEntity() {
        return Education.builder()
                .levelOfEducation(this.levelOfEducation)
                .build();
    }

    public Education toEntity(UUID educationId) {
        return Education.builder()
                .educationId(educationId)
                .levelOfEducation(this.levelOfEducation)
                .build();
    }
}

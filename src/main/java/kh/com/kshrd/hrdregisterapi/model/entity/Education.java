package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.EducationResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "educations", uniqueConstraints = {
        @UniqueConstraint(columnNames = "level_of_education")
})
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID educationId;

    @Column(nullable = false, unique = true, name = "level_of_education")
    private String levelOfEducation;

    public EducationResponse toResponse() {
        return EducationResponse.builder()
                .educationId(this.educationId)
                .levelOfEducation(this.levelOfEducation)
                .build();
    }

}

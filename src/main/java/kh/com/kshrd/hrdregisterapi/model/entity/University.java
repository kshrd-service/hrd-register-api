package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UniversityResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "universities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "abbreviation"})
})
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID universityId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String abbreviation;

    public UniversityResponse toResponse(){
        return UniversityResponse.builder()
                .universityId(this.universityId)
                .name(this.name)
                .abbreviation(this.abbreviation)
                .build();
    }

}

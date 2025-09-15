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
        @UniqueConstraint(columnNames = {"abbreviation"})
})
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID universityId;

    @Column(nullable = false, unique = true)
    private String abbreviation;

    @Column(nullable = false, unique = true)
    private Integer sortOrder;

    public UniversityResponse toResponse(){
        return UniversityResponse.builder()
                .universityId(this.universityId)
                .abbreviation(this.abbreviation)
                .sortOrder(this.sortOrder)
                .build();
    }

}

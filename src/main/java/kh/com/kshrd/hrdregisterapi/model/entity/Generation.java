package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.GenerationResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "generations", uniqueConstraints = {
        @UniqueConstraint(columnNames = "generation")
})
public class Generation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID generationId;

    @Column(nullable = false, unique = true)
    private String generation;

    @Column(nullable = false, unique = true)
    private Integer sortOrder;

    public GenerationResponse toResponse(){
        return GenerationResponse.builder()
                .generationId(this.generationId)
                .generation(this.generation)
                .sortOrder(this.sortOrder)
                .build();
    }

}

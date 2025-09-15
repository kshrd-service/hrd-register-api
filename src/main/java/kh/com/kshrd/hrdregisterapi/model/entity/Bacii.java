package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.BaciiResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "baciis", uniqueConstraints = {
        @UniqueConstraint(columnNames = "grade")
})
public class Bacii {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID baciiId;

    @Column(nullable = false, unique = true)
    private String grade;

    @Column(nullable = false, unique = true)
    private Integer sortOrder;

    public BaciiResponse toResponse(){
        return BaciiResponse.builder()
                .baciiId(this.baciiId)
                .grade(this.grade)
                .sortOrder(this.sortOrder)
                .build();
    }

}

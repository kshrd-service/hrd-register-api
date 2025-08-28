package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.com.kshrd.hrdregisterapi.model.entity.Bacii;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaciiRequest {

    @NotNull
    @NotBlank
    private String grade;

    public Bacii toEntity() {
        return Bacii.builder()
                .grade(this.grade)
                .build();
    }

    public Bacii toEntity(UUID baciiId) {
        return Bacii.builder()
                .baciiId(baciiId)
                .grade(this.grade)
                .build();
    }
}

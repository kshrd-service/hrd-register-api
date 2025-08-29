package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.com.kshrd.hrdregisterapi.model.entity.University;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityRequest {

    @NotNull
    @NotBlank
    private String abbreviation;

    public University toEntity(){
        return University.builder()
                .abbreviation(this.abbreviation)
                .build();
    }

    public University toEntity(UUID universityId){
        return University.builder()
                .universityId(universityId)
                .abbreviation(this.abbreviation)
                .build();
    }
}

package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.com.kshrd.hrdregisterapi.model.entity.Generation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerationRequest {

    @NotBlank
    @NotNull
    private String generation;

    public Generation toEntity(){
        return Generation.builder()
                .generation(this.generation)
                .build();
    }

    public Generation toEntity(UUID generationId){
        return Generation.builder()
                .generationId(generationId)
                .generation(this.generation)
                .build();
    }

}

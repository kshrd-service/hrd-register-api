package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.com.kshrd.hrdregisterapi.model.entity.Province;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceRequest {

    @NotNull
    @NotBlank
    private String name;

    public Province toEntity(){
        return Province.builder()
                .name(this.name)
                .build();
    }

    public Province toEntity(UUID provinceId){
        return Province.builder()
                .provinceId(provinceId)
                .name(this.name)
                .build();
    }
}

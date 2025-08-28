package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.com.kshrd.hrdregisterapi.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {

    @NotNull
    @NotBlank
    private String name;

    public Address toEntity() {
        return Address.builder()
                .name(this.name)
                .build();
    }

    public Address toEntity(UUID addressId) {
        return Address.builder()
                .addressId(addressId)
                .name(this.name)
                .build();
    }
}

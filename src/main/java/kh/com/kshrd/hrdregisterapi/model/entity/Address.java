package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.AddressResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "addresses", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private Integer sortOrder;

    public AddressResponse toResponse() {
        return AddressResponse.builder()
                .addressId(this.addressId)
                .name(this.name)
                .sortOrder(this.sortOrder)
                .build();
    }

}

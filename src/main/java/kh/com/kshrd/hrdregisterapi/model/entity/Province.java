package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.ProvinceResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "provinces", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID provinceId;

    @Column(nullable = false, unique = true)
    private String name;

    public ProvinceResponse toResponse(){
        return ProvinceResponse.builder()
                .provinceId(this.provinceId)
                .name(this.name)
                .build();
    }

}

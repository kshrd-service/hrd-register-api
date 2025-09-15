package kh.com.kshrd.hrdregisterapi.model.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceResponse {
    private UUID provinceId;
    private String name;
    private Integer sortOrder;
}

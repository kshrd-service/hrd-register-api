package kh.com.kshrd.hrdregisterapi.model.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityResponse {

    private UUID universityId;
    private String abbreviation;

}

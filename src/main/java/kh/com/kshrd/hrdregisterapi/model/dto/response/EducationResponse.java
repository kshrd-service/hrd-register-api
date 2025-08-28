package kh.com.kshrd.hrdregisterapi.model.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationResponse {
    private UUID educationId;
    private String levelOfEducation;
}

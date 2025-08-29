package kh.com.kshrd.hrdregisterapi.model.dto.request;

import kh.com.kshrd.hrdregisterapi.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateRequest {
    private String fullName;
    private String khFullName;
    private Gender gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private String photoUrl;
    private UUID provinceId;
    private UUID baciiId;
    private UUID universityId;
    private UUID addressId;
    private UUID educationId;
    private UUID generationId;
}

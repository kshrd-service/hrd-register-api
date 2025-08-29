package kh.com.kshrd.hrdregisterapi.model.dto.response;

import kh.com.kshrd.hrdregisterapi.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateResponse {
    private UUID candidateId;
    private String fullName;
    private String khFullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProvinceResponse provinceResponse;
    private BaciiResponse baciiResponse;
    private UniversityResponse universityResponse;
    private AddressResponse addressResponse;
    private EducationResponse educationResponse;
    private GenerationResponse generationResponse;
    private PaymentResponse paymentResponse;
}

package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import kh.com.kshrd.hrdregisterapi.annotation.MinAge;
import kh.com.kshrd.hrdregisterapi.model.entity.*;
import kh.com.kshrd.hrdregisterapi.model.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CandidateRequest {

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String khFullName;

    @NotNull
    private Gender gender;

    @NotNull
    @MinAge(15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    @NotBlank
    @Pattern(
            regexp = "^0\\d{8,9}$",
            message = "Phone number must start with 0 and contain 9–10 digits"
    )
    private String phoneNumber;


    @NotNull
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 255)
    private String photoUrl;

    @NotNull
    private UUID provinceId;

    @NotNull
    private UUID baciiId;

    @NotNull
    private UUID universityId;

    @NotNull
    private UUID addressId;

    @NotNull
    private UUID educationId;

    @NotNull
    private UUID generationId;

    public Candidate toEntity(Province province,
                              Bacii bacii,
                              University university,
                              Address address,
                              Education education,
                              Generation generation) {
        return Candidate.builder()
                .fullName(this.fullName)
                .khFullName(this.khFullName)
                .gender(this.gender)
                .dateOfBirth(this.dateOfBirth)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .photoUrl(this.photoUrl)
                .province(province)
                .bacii(bacii)
                .university(university)
                .address(address)
                .education(education)
                .generation(generation)
                .build();
    }

    public Candidate toEntity(UUID candidateId,
                              Province province,
                              Bacii bacii,
                              University university,
                              Address address,
                              Education education,
                              Generation generation) {
        return Candidate.builder()
                .candidateId(candidateId)
                .fullName(this.fullName)
                .khFullName(this.khFullName)
                .gender(this.gender)
                .dateOfBirth(this.dateOfBirth)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .photoUrl(this.photoUrl)
                .province(province)
                .bacii(bacii)
                .university(university)
                .address(address)
                .education(education)
                .generation(generation)
                .build();
    }
}

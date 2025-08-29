package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponse;
import kh.com.kshrd.hrdregisterapi.model.enums.Gender;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "candidates",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone_number")
        })
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID candidateId;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String khFullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateOfBirth;

    @Column(nullable = false, length = 20, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String photoUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false, foreignKey = @ForeignKey(name = "fk_candidate_province"))
    @ToString.Exclude
    private Province province;

    @ManyToOne
    @JoinColumn(name = "bacii_id", nullable = false, foreignKey = @ForeignKey(name = "fk_candidate_bacii"))
    @ToString.Exclude
    private Bacii bacii;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false, foreignKey = @ForeignKey(name = "fk_candidate_university"))
    @ToString.Exclude
    private University university;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "fk_candidate_address"))
    @ToString.Exclude
    private Address address;

    @ManyToOne
    @JoinColumn(name = "education_id", nullable = false, foreignKey = @ForeignKey(name = "fk_candidate_education"))
    @ToString.Exclude
    private Education education;

    @ManyToOne
    @JoinColumn(name = "generation_id", nullable = false, foreignKey = @ForeignKey(name = "fk_candidate_generation"))
    @ToString.Exclude
    private Generation generation;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.PERSIST)
    private Payment payment;

    public CandidateResponse toResponse() {
        return CandidateResponse.builder()
                .candidateId(this.candidateId)
                .fullName(this.fullName)
                .khFullName(this.khFullName)
                .gender(this.gender)
                .dateOfBirth(this.dateOfBirth)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .photoUrl(this.photoUrl)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .provinceResponse(this.province.toResponse())
                .baciiResponse(this.bacii.toResponse())
                .universityResponse(this.university.toResponse())
                .addressResponse(this.address.toResponse())
                .educationResponse(this.education.toResponse())
                .generationResponse(this.generation.toResponse())
                .paymentResponse(this.payment.toResponse())
                .build();
    }

}

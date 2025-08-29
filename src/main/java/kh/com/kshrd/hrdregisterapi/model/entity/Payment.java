package kh.com.kshrd.hrdregisterapi.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PaymentResponse;
import kh.com.kshrd.hrdregisterapi.model.enums.Currency;
import kh.com.kshrd.hrdregisterapi.model.enums.Method;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Method method;

    @Column(length = 120)
    private String billNo;

    @Column(length = 120)
    private String payerName;

    @Column(length = 2048)
    private String khqrPayload;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Currency currency;

    @Column(nullable = false)
    private Boolean paid;

    @Column
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_candidate"))
    private Candidate candidate;

    public PaymentResponse toResponse() {
        return PaymentResponse.builder()
                .paymentId(this.paymentId)
                .method(this.method)
                .billNo(this.billNo)
                .payerName(this.payerName)
                .khqrPayload(this.khqrPayload)
                .amount(this.amount)
                .currency(this.currency)
                .paid(this.paid)
                .paidAt(this.paidAt)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}

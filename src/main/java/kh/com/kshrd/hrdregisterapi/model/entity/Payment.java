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
//@ToString
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Method method;

    @Column(nullable = false, length = 120)
    private String billNo;

    @Column(nullable = false, length = 120)
    private String accountNo;

    @Column(nullable = false, length = 2048)
    private String khqrData;

    @Column(length = 120)
    private String payerBankName;

    @Column(length = 120)
    private String payerAccountNo;

    @Column(length = 120)
    private String payerName;

    @Column(length = 120)
    private String payerRemark;

    @Column(precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Currency currency;

    @Column(length = 20)
    private String paidStatus;

    @Column
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private Boolean isSend;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "candidate_id", foreignKey = @ForeignKey(name = "fk_payment_candidate"))
    private Candidate candidate;

    public PaymentResponse toResponse() {
        return PaymentResponse.builder()
                .paymentId(this.paymentId)
                .method(this.method)
                .billNo(this.billNo)
                .accountNo(this.accountNo)
                .khqrData(this.khqrData)
                .payerAccountNo(this.payerAccountNo)
                .payerBankName(this.payerBankName)
                .payerName(this.payerName)
                .payerRemark(this.payerRemark)
                .amount(this.amount)
                .currency(this.currency)
                .paidStatus(this.paidStatus)
                .paidAt(this.paidAt)
                .isSend(this.isSend)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}

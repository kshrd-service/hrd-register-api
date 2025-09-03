package kh.com.kshrd.hrdregisterapi.model.dto.response;

import kh.com.kshrd.hrdregisterapi.model.enums.Currency;
import kh.com.kshrd.hrdregisterapi.model.enums.Method;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private UUID paymentId;
    private Method method;
    private String billNo;
    private String accountNo;
    private String khqrData;
    private String payerBankName;
    private String payerAccountNo;
    private String payerName;
    private String payerRemark;
    private BigDecimal amount;
    private Currency currency;
    private String paidStatus;
    private LocalDateTime paidAt;
    private Boolean isSend;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

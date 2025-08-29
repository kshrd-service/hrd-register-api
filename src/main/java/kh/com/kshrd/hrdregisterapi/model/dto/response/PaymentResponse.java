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
    private String payerName;
    private String khqrPayload;
    private BigDecimal amount;
    private Currency currency;
    private Boolean paid;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

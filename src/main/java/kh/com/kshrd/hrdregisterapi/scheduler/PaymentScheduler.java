package kh.com.kshrd.hrdregisterapi.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import kh.com.kshrd.hrdregisterapi.model.entity.Payment;
import kh.com.kshrd.hrdregisterapi.model.enums.Currency;
import kh.com.kshrd.hrdregisterapi.repository.PaymentRepository;
import kh.com.kshrd.hrdregisterapi.service.CandidateService;
import kh.com.kshrd.hrdregisterapi.service.PaymentService;
import kh.com.kshrd.hrdregisterapi.service.WebillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentScheduler {

    private final WebillService webillService;
    private final PaymentRepository paymentRepository;
    private final CandidateService candidateService;
    private final PaymentService paymentService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");

    @Scheduled(fixedDelay = 60_000)
    public void checkPendingPayments() {
        final List<String> billNos = safeList(paymentRepository.findAllBillNoNotPaid());
        if (billNos.isEmpty()) return;

        final List<Payment> dirty = new ArrayList<>();

        for (String billNo : billNos) {
            if (billNo == null || billNo.isBlank()) continue;

            try {
                processSingleBillNo(billNo).ifPresent(dirty::add);
            } catch (Exception ex) {
                log.warn("Failed to check payment for billNo {}: {}", billNo, ex.getMessage());
            }
        }

        if (!dirty.isEmpty()) {
            paymentService.persistUpdatedPayments(dirty);
        }
    }

    private Optional<Payment> processSingleBillNo(String billNo) throws JsonProcessingException {
        final JsonNode result = webillService.checkPaymentStatus(billNo);
        final JsonNode data = result.path("data");
        if (!data.isArray() || data.isEmpty()) return Optional.empty();

        final Payment payment = paymentRepository.findByBillNo(billNo).orElse(null);
        if (payment == null) return Optional.empty();

        final JsonNode node = data.get(0);
        if (node == null || node.isNull()) return Optional.empty();

        updatePayment(payment, node);
        return Optional.of(payment);
    }


    @Scheduled(fixedDelay = 60_000)
    public void checkPaymentAndSendApplicationForm() {
        final List<Payment> payments = safeList(paymentRepository.findAllPaidAndNotSent());
        if (payments.isEmpty()) return;

        final List<Payment> sentPayments = new ArrayList<>();

        for (Payment p : payments) {
            if (p.getCandidate() == null) continue;

            try {
                candidateService.sendApplicationForm(p.getCandidate().getCandidateId());
                p.setIsSend(true);
                sentPayments.add(p);
            } catch (Exception ex) {
                log.warn("Failed to send application form for candidate {} (billNo={}): {}",
                        p.getCandidate().getCandidateId(), p.getBillNo(), ex.getMessage());
            }
        }

        if (!sentPayments.isEmpty()) {
            paymentService.markAsSent(sentPayments);
        }
    }


    private void updatePayment(Payment payment, JsonNode node) {
        String newStatus = node.path("status_name").asText("");
        if (!Objects.equals(payment.getPaidStatus(), newStatus)) {
            payment.setPaidStatus(newStatus);
        }

        JsonNode tx = firstTransaction(node);
        if (tx == null) return;

        LocalDateTime newPaidAt = parseDateTime(
                tx.path("transaction_date").asText(null),
                tx.path("transaction_time").asText(null)
        );
        BigDecimal newAmount = parseAmount(tx.path("amount").asText(null));
        Currency newCurrency = parseCurrency(tx.path("currency_code").asText(null));
        String newBankName = tx.path("sender_bank_name").asText(null);
        String newAccountNo = tx.path("sender_account_no").asText(null);
        String newPayerName = tx.path("sender_account_name").asText(null);
        String newRemark = tx.path("sender_remark").asText(null);

        if (!Objects.equals(payment.getPaidAt(), newPaidAt)) {
            payment.setPaidAt(newPaidAt);
        }
        if (!Objects.equals(payment.getAmount(), newAmount)) {
            payment.setAmount(newAmount);
        }
        if (!Objects.equals(payment.getCurrency(), newCurrency)) {
            payment.setCurrency(newCurrency);
        }
        if (!Objects.equals(payment.getPayerBankName(), newBankName)) {
            payment.setPayerBankName(newBankName);
        }
        if (!Objects.equals(payment.getPayerAccountNo(), newAccountNo)) {
            payment.setPayerAccountNo(newAccountNo);
        }
        if (!Objects.equals(payment.getPayerName(), newPayerName)) {
            payment.setPayerName(newPayerName);
        }
        if (!Objects.equals(payment.getPayerRemark(), newRemark)) {
            payment.setPayerRemark(newRemark);
        }
    }


    private JsonNode firstTransaction(JsonNode node) {
        JsonNode txs = node.path("transactions");
        return (txs.isArray() && !txs.isEmpty()) ? txs.get(0) : null;
    }

    private LocalDateTime parseDateTime(String date, String time) {
        if (date == null || time == null) return null;
        try {
            return LocalDateTime.parse(date + " " + time, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseAmount(String amount) {
        if (amount == null || amount.isBlank()) return null;
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Currency parseCurrency(String code) {
        if (code == null || code.isBlank()) return null;
        try {
            return Currency.valueOf(code.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static <T> List<T> safeList(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
}

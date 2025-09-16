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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentScheduler {

    private final WebillService webillService;
    private final PaymentRepository paymentRepository;
    private final CandidateService candidateService;
    private final PaymentService paymentService;

    private static final int BATCH_SIZE = 100;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");

    @Scheduled(fixedDelay = 60_000)
    public void checkPendingPayments() {
        final List<String> billNos = safeList(paymentRepository.findAllBillNoNotPaid());
        if (billNos.isEmpty()) return;

        for (int start = 0; start < billNos.size(); start += BATCH_SIZE) {
            final List<String> chunk = billNos.subList(start, Math.min(start + BATCH_SIZE, billNos.size()));
            try {
                processChunkIndexPaired(chunk);
            } catch (Exception ignored) {
            }
        }
    }

    private void processChunkIndexPaired(List<String> chunk) throws JsonProcessingException {
        final JsonNode result = webillService.checkPaymentStatus(chunk);
        final JsonNode data = result.path("data");
        if (!data.isArray() || data.isEmpty()) return;

        final List<Payment> payments = safeList(paymentRepository.findAllByBillNoIn(chunk));
        if (payments.isEmpty()) return;

        final Map<String, Integer> chunkIndex = new HashMap<>(chunk.size());
        for (int i = 0; i < chunk.size(); i++) {
            final String billNo = chunk.get(i);
            if (billNo != null && !billNo.isBlank()) {
                chunkIndex.putIfAbsent(billNo, i);
            }
        }

        final List<Payment> dirty = new ArrayList<>();
        for (Payment payment : payments) {
            final String dbBillNo = payment.getBillNo();
            if (dbBillNo == null || dbBillNo.isBlank()) continue;

            final Integer idx = chunkIndex.get(dbBillNo);
            if (idx == null || idx < 0 || idx >= data.size()) continue;

            final JsonNode node = data.get(idx);
            if (node == null || node.isNull()) continue;

            updatePayment(payment, node);
            dirty.add(payment);
        }

        if (!dirty.isEmpty()) {
            paymentService.persistUpdatedPayments(dirty);
        }
    }


    @Scheduled(fixedDelay = 60_000)
    public void checkPaymentAndSendApplicationForm() {
        final List<Payment> payments = safeList(paymentRepository.findAllPaidAndNotSent());
        if (payments.isEmpty()) return;

        for (Payment p : payments) {
            if (p.getCandidate() == null) continue;
            try {
                candidateService.sendApplicationForm(p.getCandidate().getCandidateId());
                p.setIsSend(true);
            } catch (Exception ignored) {
            }
        }

        paymentService.markAsSent(payments);
    }

    private void updatePayment(Payment payment, JsonNode node) {
        payment.setPaidStatus(node.path("status_name").asText(""));

        JsonNode tx = firstTransaction(node);
        if (tx == null) return;

        payment.setPaidAt(parseDateTime(
                tx.path("transaction_date").asText(null),
                tx.path("transaction_time").asText(null)
        ));
        payment.setAmount(parseAmount(tx.path("amount").asText(null)));
        payment.setCurrency(parseCurrency(tx.path("currency_code").asText(null)));

        payment.setPayerBankName(tx.path("sender_bank_name").asText(null));
        payment.setPayerAccountNo(tx.path("sender_account_no").asText(null));
        payment.setPayerName(tx.path("sender_account_name").asText(null));
        payment.setPayerRemark(tx.path("sender_remark").asText(null));
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

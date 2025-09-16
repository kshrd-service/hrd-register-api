package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import jakarta.transaction.Transactional;
import kh.com.kshrd.hrdregisterapi.model.entity.Payment;
import kh.com.kshrd.hrdregisterapi.repository.PaymentRepository;
import kh.com.kshrd.hrdregisterapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void persistUpdatedPayments(List<Payment> updated) {
        if (updated == null || updated.isEmpty()) return;
        for (Payment payment : updated) {
            if (payment.getBillNo() == null || payment.getBillNo().isBlank()) continue;
            paymentRepository.updateByBillNo(payment);
        }
    }

    @Transactional
    public void markAsSent(List<Payment> payments) {
        if (payments == null || payments.isEmpty()) return;

        List<String> billNos = payments.stream()
                .map(Payment::getBillNo)
                .filter(b -> b != null && !b.isBlank())
                .toList();

        if (!billNos.isEmpty()) {
            paymentRepository.markAsSentByBillNos(billNos);
        }
    }

}

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
        paymentRepository.saveAll(updated);
    }

    @Transactional
    public void markAsSent(List<Payment> payments) {
        if (payments == null || payments.isEmpty()) return;
        paymentRepository.saveAll(payments);
    }

}

package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.entity.Payment;

import java.util.List;

public interface PaymentService {

    void persistUpdatedPayments(List<Payment> updated);

    void markAsSent(List<Payment> payments);

}

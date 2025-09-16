package kh.com.kshrd.hrdregisterapi.repository;

import kh.com.kshrd.hrdregisterapi.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("""
            SELECT p.billNo
            FROM Payment p
            WHERE p.billNo IS NOT NULL
              AND p.billNo <> ''
              AND (p.paidStatus IS NULL OR UPPER(TRIM(p.paidStatus)) <> 'PAID')
            """)
    List<String> findAllBillNoNotPaid();

    @Query("""
            SELECT p
            FROM Payment p
            WHERE UPPER(TRIM(p.paidStatus)) = 'PAID'
              AND (p.isSend IS NULL OR p.isSend = FALSE)
            """)
    List<Payment> findAllPaidAndNotSent();

    @Query("""
            SELECT p
            FROM Payment p
            WHERE p.billNo IN :billNos
            """)
    List<Payment> findAllByBillNoIn(List<String> billNos);

    @Modifying
    @Query("""
                UPDATE Payment p
                SET p.paidStatus = :#{#payment.paidStatus},
                    p.paidAt = :#{#payment.paidAt},
                    p.amount = :#{#payment.amount},
                    p.currency = :#{#payment.currency},
                    p.payerBankName = :#{#payment.payerBankName},
                    p.payerAccountNo = :#{#payment.payerAccountNo},
                    p.payerName = :#{#payment.payerName},
                    p.payerRemark = :#{#payment.payerRemark},
                    p.updatedAt = CURRENT_TIMESTAMP
                WHERE p.billNo = :#{#payment.billNo}
            """)
    void updateByBillNo(@Param("payment") Payment payment);

    @Modifying
    @Query("""
        UPDATE Payment p
        SET p.isSend = true,
            p.updatedAt = CURRENT_TIMESTAMP
        WHERE p.billNo IN :billNos
    """)
    void markAsSentByBillNos(@Param("billNos") List<String> billNos);
}


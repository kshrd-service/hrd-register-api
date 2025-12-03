package kh.com.kshrd.hrdregisterapi.repository;

import kh.com.kshrd.hrdregisterapi.model.entity.Candidate;
import kh.com.kshrd.hrdregisterapi.model.entity.Generation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    boolean existsByEmailIgnoreCase(String email);

    Page<Candidate> findAllByGeneration(Generation generation, Pageable pageable);

    List<Candidate> findAllByPaymentPaidStatus(String paidStatus);

}

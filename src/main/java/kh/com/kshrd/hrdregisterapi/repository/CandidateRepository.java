package kh.com.kshrd.hrdregisterapi.repository;

import kh.com.kshrd.hrdregisterapi.model.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    boolean existsByEmailOrPhoneNumberIgnoreCase(String email, String phoneNumber);
}

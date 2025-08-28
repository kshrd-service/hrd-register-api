package kh.com.kshrd.hrdregisterapi.repository;

import kh.com.kshrd.hrdregisterapi.model.entity.Generation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenerationRepository extends JpaRepository<Generation, UUID> {
    boolean existsByGenerationIgnoreCase(String generation);
}

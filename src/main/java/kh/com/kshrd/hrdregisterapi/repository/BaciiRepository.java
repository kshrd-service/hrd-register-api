package kh.com.kshrd.hrdregisterapi.repository;

import kh.com.kshrd.hrdregisterapi.model.entity.Bacii;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BaciiRepository extends JpaRepository<Bacii, UUID> {
    boolean existsByGradeIgnoreCase(String grade);
}

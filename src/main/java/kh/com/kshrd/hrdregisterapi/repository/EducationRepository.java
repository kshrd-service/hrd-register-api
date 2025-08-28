package kh.com.kshrd.hrdregisterapi.repository;

import kh.com.kshrd.hrdregisterapi.model.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EducationRepository extends JpaRepository<Education, UUID> {
    boolean existsByLevelOfEducationIgnoreCase(String levelOfEducation);
}

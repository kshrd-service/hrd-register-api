package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.UniversityRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UniversityResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface UniversityService {

    UniversityResponse createUniversity(UniversityRequest request);

    UniversityResponse getUniversityById(UUID universityId);

    PagedResponse<List<UniversityResponse>> getAllUniversities(int page, int size, String sortBy, Sort.Direction direction);

    UniversityResponse updateUniversityById(UUID universityId, UniversityRequest request);

    void deleteUniversityById(UUID universityId);
}

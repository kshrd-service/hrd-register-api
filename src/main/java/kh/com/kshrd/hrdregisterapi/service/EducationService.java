package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.EducationRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.EducationResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface EducationService {
    EducationResponse createEducation(EducationRequest request);

    EducationResponse getEducationById(UUID educationId);

    PagedResponse<List<EducationResponse>> getAllEducations(int page, int size, String sortBy, Sort.Direction direction);

    EducationResponse updateEducationById(UUID educationId, EducationRequest request);

    void deleteEducationById(UUID educationId);
}

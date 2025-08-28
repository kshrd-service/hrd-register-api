package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.GenerationRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.GenerationResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface GenerationService {
    GenerationResponse createGeneration(GenerationRequest request);

    GenerationResponse getGenerationById(UUID generationId);

    PagedResponse<List<GenerationResponse>> getAllGenerations(int page, int size, String sortBy, Sort.Direction direction);

    GenerationResponse updateGenerationById(UUID generationId, GenerationRequest request);

    void deleteGenerationById(UUID generationId);
}

package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.GenerationRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.GenerationResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.Generation;
import kh.com.kshrd.hrdregisterapi.repository.GenerationRepository;
import kh.com.kshrd.hrdregisterapi.service.GenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.pageResponse;

@Service
@RequiredArgsConstructor
public class GenerationServiceImpl implements GenerationService {

    private final GenerationRepository generationRepository;

    @Override
    public GenerationResponse createGeneration(GenerationRequest request) {
        if (generationRepository.existsByGenerationIgnoreCase(request.getGeneration())) {
            throw new ConflictException("Generation name already exists");
        }
        Generation saved = generationRepository.save(request.toEntity());
        return saved.toResponse();
    }

    @Override
    public GenerationResponse getGenerationById(UUID generationId) {
        Generation generation = generationRepository.findById(generationId).orElseThrow(
                () -> new NotFoundException("Generation not " + generationId + " found")
        );
        return generation.toResponse();
    }

    @Override
    public PagedResponse<List<GenerationResponse>> getAllGenerations(
            int page, int size, String sortBy, Sort.Direction direction
    ) {
        int zeroBased = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(zeroBased, size, Sort.by(direction, sortBy));
        Page<Generation> pageGenerations = generationRepository.findAll(pageable);

        List<GenerationResponse> items = pageGenerations
                .getContent()
                .stream()
                .map(Generation::toResponse)
                .toList();

        return pageResponse(
                items,
                pageGenerations.getTotalElements(),
                page,
                size,
                pageGenerations.getTotalPages()
        );
    }

    @Override
    public GenerationResponse updateGenerationById(UUID generationId, GenerationRequest request) {
        Generation generation = generationRepository.findById(generationId).orElseThrow(
                () -> new NotFoundException("Generation not " + generationId + " found")
        );

        if (generationRepository.existsByGenerationIgnoreCase(generation.getGeneration())) {
            throw new ConflictException("Generation name already exists");
        }

        Generation saved = generationRepository.save(request.toEntity(generationId));
        return saved.toResponse();
    }

    @Override
    public void deleteGenerationById(UUID generationId) {
        Generation generation = generationRepository.findById(generationId).orElseThrow(
                () -> new NotFoundException("Generation not " + generationId + " found")
        );
        generationRepository.deleteById(generation.getGenerationId());
    }
}

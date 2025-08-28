package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.UniversityRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UniversityResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.University;
import kh.com.kshrd.hrdregisterapi.repository.UniversityRepository;
import kh.com.kshrd.hrdregisterapi.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.pageResponse;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    @Override
    public UniversityResponse createUniversity(UniversityRequest request) {
        if (universityRepository.existsByNameAndAbbreviationIgnoreCase(request.getName(), request.getAbbreviation())) {
            throw new ConflictException("University name and abbreviation already exists");
        }
        University saved = universityRepository.save(request.toEntity());
        return saved.toResponse();
    }

    @Override
    public UniversityResponse getUniversityById(UUID universityId) {
        University university = universityRepository.findById(universityId).orElseThrow(
                () -> new NotFoundException("University not " + universityId + " found")
        );
        return university.toResponse();
    }

    @Override
    public PagedResponse<List<UniversityResponse>> getAllUniversities(
            int page, int size, String sortBy, Sort.Direction direction
    ) {
        int zeroBased = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(zeroBased, size, Sort.by(direction, sortBy));
        Page<University> pageUniversities = universityRepository.findAll(pageable);

        List<UniversityResponse> items = pageUniversities
                .getContent()
                .stream()
                .map(University::toResponse)
                .toList();

        return pageResponse(
                items,
                pageUniversities.getTotalElements(),
                page,
                size,
                pageUniversities.getTotalPages()
        );
    }

    @Override
    public UniversityResponse updateUniversityById(UUID universityId, UniversityRequest request) {
        University university = universityRepository.findById(universityId).orElseThrow(
                () -> new NotFoundException("University not " + universityId + " found")
        );

        if (universityRepository.existsByNameAndAbbreviationIgnoreCase(request.getName(), request.getAbbreviation())) {
            throw new ConflictException("University name already exists");
        }

        University saved = universityRepository.save(request.toEntity(universityId));
        return saved.toResponse();
    }

    @Override
    public void deleteUniversityById(UUID universityId) {
        University university = universityRepository.findById(universityId).orElseThrow(
                () -> new NotFoundException("University not " + universityId + " found")
        );
        universityRepository.deleteById(university.getUniversityId());
    }
}

package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.EducationRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.EducationResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.Education;
import kh.com.kshrd.hrdregisterapi.repository.EducationRepository;
import kh.com.kshrd.hrdregisterapi.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.pageResponse;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    @Override
    @Transactional
    public EducationResponse createEducation(EducationRequest request) {
        if (educationRepository.existsByLevelOfEducationIgnoreCase(request.getLevelOfEducation())) {
            throw new ConflictException("Level Of Education already exists");
        }
        Education saved = educationRepository.save(request.toEntity());
        return saved.toResponse();
    }

    @Override
    public EducationResponse getEducationById(UUID educationId) {
        Education education = educationRepository.findById(educationId).orElseThrow(
                () -> new NotFoundException("Education not " + educationId + " found")
        );
        return education.toResponse();
    }

    @Override
    public PagedResponse<List<EducationResponse>> getAllEducations(int page, int size, String sortBy, Sort.Direction direction) {
        int zeroBased = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(zeroBased, size, Sort.by(direction, sortBy));
        Page<Education> pageSubjects = educationRepository.findAll(pageable);

        List<EducationResponse> items = pageSubjects
                .getContent()
                .stream()
                .map(Education::toResponse)
                .toList();

        return pageResponse(
                items,
                pageSubjects.getTotalElements(),
                page,
                size,
                pageSubjects.getTotalPages()
        );
    }

    @Override
    @Transactional
    public EducationResponse updateEducationById(UUID educationId, EducationRequest request) {
        Education education = educationRepository.findById(educationId).orElseThrow(
                () -> new NotFoundException("Education not " + educationRepository + " found")
        );

        if (educationRepository.existsByLevelOfEducationIgnoreCase(education.getLevelOfEducation())) {
            throw new ConflictException("Level Of Education already exists");
        }

        Education saved = educationRepository.save(request.toEntity(educationId));
        return saved.toResponse();
    }

    @Override
    @Transactional
    public void deleteEducationById(UUID educationId) {
        Education education = educationRepository.findById(educationId).orElseThrow(
                () -> new NotFoundException("Education not " + educationId + " found")
        );
        educationRepository.deleteById(education.getEducationId());
    }
}

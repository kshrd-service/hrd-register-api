package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.CandidateRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.*;
import kh.com.kshrd.hrdregisterapi.model.enums.Currency;
import kh.com.kshrd.hrdregisterapi.model.enums.Method;
import kh.com.kshrd.hrdregisterapi.repository.*;
import kh.com.kshrd.hrdregisterapi.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.pageResponse;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final ProvinceRepository provinceRepository;
    private final BaciiRepository baciiRepository;
    private final UniversityRepository universityRepository;
    private final AddressRepository addressRepository;
    private final EducationRepository educationRepository;
    private final GenerationRepository generationRepository;

    @Override
    @Transactional
    public CandidateResponse registerCandidate(CandidateRequest request) {

        if (candidateRepository.existsByEmailAndPhoneNumberIgnoreCase(request.getEmail(), request.getPhoneNumber())) {
            throw new ConflictException("Candidate email and phone number already exists");
        }

        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new NotFoundException("Province not " + request.getProvinceId() + " found"));

        Bacii bacii = baciiRepository.findById(request.getBaciiId())
                .orElseThrow(() -> new NotFoundException("Bacii not " + request.getBaciiId() + " found"));

        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new NotFoundException("University not " + request.getUniversityId() + " found"));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new NotFoundException("Address not " + request.getAddressId() + " found"));

        Education education = educationRepository.findById(request.getEducationId())
                .orElseThrow(() -> new NotFoundException("Education not " + request.getEducationId() + " found"));

        Generation generation = generationRepository.findById(request.getGenerationId())
                .orElseThrow(() -> new NotFoundException("Generation not " + request.getGenerationId() + " found"));

        Payment payment = Payment.builder()
                .method(Method.QR)
                .khqrPayload("")
                .amount(BigDecimal.valueOf(5))
                .currency(Currency.USD)
                .paid(false)
                .build();

        Candidate candidate = request.toEntity(province, bacii, university, address, education, generation);
        candidate.setPayment(payment);
        payment.setCandidate(candidate);

        Candidate saved = candidateRepository.saveAndFlush(candidate);
        return saved.toResponse();
    }

    @Override
    public CandidateResponse getCandidateById(UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );
        return candidate.toResponse();
    }

    @Override
    public PagedResponse<List<CandidateResponse>> getAllCandidates(
            int page, int size, String sortBy, Sort.Direction direction
    ) {
        int zeroBased = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(zeroBased, size, Sort.by(direction, sortBy));
        Page<Candidate> pageCandidates = candidateRepository.findAll(pageable);

        List<CandidateResponse> items = pageCandidates
                .getContent()
                .stream()
                .map(Candidate::toResponse)
                .toList();

        return pageResponse(
                items,
                pageCandidates.getTotalElements(),
                page,
                size,
                pageCandidates.getTotalPages()
        );
    }

    @Override
    @Transactional
    public CandidateResponse updateCandidateById(UUID candidateId, CandidateRequest request) {
        Candidate existing = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );

        if (candidateRepository.existsByEmailAndPhoneNumberIgnoreCase(request.getEmail(), request.getPhoneNumber())) {
            throw new ConflictException("Candidate email and phone number already exists");
        }

        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new NotFoundException("Province not " + request.getProvinceId() + " found"));

        Bacii bacii = baciiRepository.findById(request.getBaciiId())
                .orElseThrow(() -> new NotFoundException("Bacii not " + request.getBaciiId() + " found"));

        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new NotFoundException("University not " + request.getUniversityId() + " found"));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new NotFoundException("Address not " + request.getAddressId() + " found"));

        Education education = educationRepository.findById(request.getEducationId())
                .orElseThrow(() -> new NotFoundException("Education not " + request.getEducationId() + " found"));

        Generation generation = generationRepository.findById(request.getGenerationId())
                .orElseThrow(() -> new NotFoundException("Generation not " + request.getGenerationId() + " found"));

        Payment payment = existing.getPayment();

        Candidate toSave = request.toEntity(candidateId, province, bacii, university, address, education, generation);
        toSave.setPayment(payment);
        if (payment != null) payment.setCandidate(toSave);

        Candidate saved = candidateRepository.saveAndFlush(toSave);
        return saved.toResponse();
    }

    @Override
    @Transactional
    public void deleteCandidateById(UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );
        candidateRepository.deleteById(candidate.getCandidateId());
    }
}

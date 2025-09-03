package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import kh.com.kshrd.hrdregisterapi.component.KhqrRenderer;
import kh.com.kshrd.hrdregisterapi.component.QrGenerator;
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
import kh.com.kshrd.hrdregisterapi.service.EmailService;
import kh.com.kshrd.hrdregisterapi.service.PdfService;
import kh.com.kshrd.hrdregisterapi.service.WebillService;
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
    private final EmailService emailService;
    private final PdfService pdfService;
    private final QrGenerator qrGenerator;
    private final KhqrRenderer khqrRenderer;
    private final WebillService webillService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public CandidateResponse registerCandidate(CandidateRequest request) throws Exception {

        if (candidateRepository.existsByEmailOrPhoneNumberIgnoreCase(request.getEmail(), request.getPhoneNumber())) {
            throw new ConflictException("Candidate email or phone number already exists");
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

        JsonNode root = webillService.createQuickBill();

        String billNo = root.path("data").path("bill_no").asText(null);
        String accountNo = root.path("data").path("account_no").asText(null);
        String khqrData = root.path("data").path("khqr_data").asText(null);

        if (billNo == null || billNo.isBlank()) {
            throw new NotFoundException("BillNo not found in response: " + root);
        }

        if (accountNo == null || accountNo.isBlank()) {
            throw new NotFoundException("AccountNo not found in response: " + root);
        }

        if (khqrData == null || khqrData.isBlank()) {
            throw new NotFoundException("Khqr Data not found in response: " + root);
        }

        Payment payment = Payment.builder()
                .method(Method.QR)
                .billNo(billNo)
                .accountNo(accountNo)
                .khqrData(khqrData)
                .isSend(false)
                .build();

        Candidate candidate = request.toEntity(province, bacii, university, address, education, generation);
        candidate.setPayment(payment);
        if (payment != null) payment.setCandidate(candidate);

        Candidate saved = candidateRepository.saveAndFlush(candidate);

        sendDonationForm(saved.getCandidateId());

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

        if (candidateRepository.existsByEmailOrPhoneNumberIgnoreCase(request.getEmail(), request.getPhoneNumber())) {
            throw new ConflictException("Candidate email or phone number already exists");
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

        Candidate candidate = request.toEntity(candidateId, province, bacii, university, address, education, generation);
        candidate.setPayment(payment);
        if (payment != null) payment.setCandidate(candidate);

        Candidate saved = candidateRepository.saveAndFlush(candidate);
        return saved.toResponse();
    }

    @Override
    @Transactional
    public void deleteCandidateById(UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );
        candidate.setPayment(null);
        candidateRepository.deleteById(candidate.getCandidateId());
    }

    @Override
    public void sendApplicationForm(UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );

        byte[] pdfBytes = pdfService.generatePdf(candidate);

        emailService.sendApplicationForm(candidate, pdfBytes);
    }

    @Override
    public void resendApplicationForm(UUID candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );

        byte[] pdfBytes = pdfService.generatePdf(candidate);

        emailService.sendApplicationForm(candidate, pdfBytes);
    }

    @Override
    public void sendDonationForm(UUID candidateId) throws Exception {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );

        String khqrData = candidate.getPayment().getKhqrData();

        if (khqrData == null || khqrData.isBlank()) {
            throw new NotFoundException("KHQR data for candidate " + candidateId + " not found");
        }

        byte[] qr = qrGenerator.toHighResPng(khqrData);
        byte[] card = khqrRenderer.render(qr);

        emailService.sendDonationForm(candidate, card);
    }

    @Override
    public void resendDonationForm(UUID candidateId) throws Exception {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new NotFoundException("Candidate not " + candidateId + " found")
        );

        JsonNode root = webillService.createQuickBill();

        String billNo = root.path("data").path("bill_no").asText(null);
        String accountNo = root.path("data").path("account_no").asText(null);
        String khqrData = root.path("data").path("khqr_data").asText(null);

        if (billNo == null || billNo.isBlank()) {
            throw new NotFoundException("BillNo not found in response: " + root);
        }

        if (accountNo == null || accountNo.isBlank()) {
            throw new NotFoundException("AccountNo not found in response: " + root);
        }

        if (khqrData == null || khqrData.isBlank()) {
            throw new NotFoundException("Khqr Data not found in response: " + root);
        }

        Payment payment = candidate.getPayment();
        payment.setMethod(Method.QR);
        payment.setBillNo(billNo);
        payment.setAccountNo(accountNo);
        payment.setKhqrData(khqrData);
        payment.setPayerAccountNo(null);
        payment.setPayerBankName(null);
        payment.setPayerName(null);
        payment.setPayerRemark(null);
        payment.setPaidStatus(null);
        payment.setPaidAt(null);
        payment.setIsSend(false);

        paymentRepository.save(payment);

        byte[] qr = qrGenerator.toHighResPng(khqrData);
        byte[] card = khqrRenderer.render(qr);

        emailService.sendDonationForm(candidate, card);
    }

}

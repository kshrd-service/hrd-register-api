package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.CandidateRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponseAdmin;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.Candidate;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface CandidateService {

    CandidateResponse registerCandidate(CandidateRequest request) throws Exception;

    CandidateResponseAdmin getCandidateById(UUID candidateId);

    PagedResponse<List<CandidateResponse>> getAllCandidates(int page, int size, String sortBy, Sort.Direction direction);

    CandidateResponseAdmin updateCandidateById(UUID candidateId, CandidateRequest request);

    void deleteCandidateById(UUID candidateId);

    void sendApplicationForm(UUID candidateId);

    void resendApplicationForm(UUID candidateId);

    void sendDonationForm(UUID candidateId) throws Exception;

    void resendDonationForm(UUID candidateId) throws Exception;

    PagedResponse<List<CandidateResponseAdmin>> getAllCandidatesAdmin(int page, int size, String sortBy, Sort.Direction direction);

    PagedResponse<List<CandidateResponseAdmin>> getAllCandidatesAdminByGenerationId(UUID generationId, int page, int size, String sortBy, Sort.Direction direction);

    PagedResponse<List<CandidateResponse>> getAllCandidatesByGenerationId(UUID generationId, int page, int size, String sortBy, Sort.Direction direction);

    List<Candidate> getAllCandidatesPaid();
}

package kh.com.kshrd.hrdregisterapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.CandidateRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface CandidateService {

    CandidateResponse registerCandidate(CandidateRequest request) throws Exception;

    CandidateResponse getCandidateById(UUID candidateId);

    PagedResponse<List<CandidateResponse>> getAllCandidates(int page, int size, String sortBy, Sort.Direction direction);

    CandidateResponse updateCandidateById(UUID candidateId, CandidateRequest request);

    void deleteCandidateById(UUID candidateId);

    void sendApplicationForm(UUID candidateId);

    void resendApplicationForm(UUID candidateId);

    void sendDonationForm(UUID candidateId) throws Exception;

    void resendDonationForm(UUID candidateId) throws Exception;
}

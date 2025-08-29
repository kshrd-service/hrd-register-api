package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.CandidateRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface CandidateService {

    CandidateResponse registerCandidate(CandidateRequest request);

    CandidateResponse getCandidateById(UUID candidateId);

    PagedResponse<List<CandidateResponse>> getAllCandidates(int page, int size, String sortBy, Sort.Direction direction);

    CandidateResponse updateCandidateById(UUID candidateId, CandidateRequest request);

    void deleteCandidateById(UUID candidateId);

}

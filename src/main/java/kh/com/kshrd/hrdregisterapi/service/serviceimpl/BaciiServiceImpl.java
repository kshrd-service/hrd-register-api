package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.BaciiRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.BaciiResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.Bacii;
import kh.com.kshrd.hrdregisterapi.repository.BaciiRepository;
import kh.com.kshrd.hrdregisterapi.service.BaciiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.pageResponse;

@Service
@RequiredArgsConstructor
public class BaciiServiceImpl implements BaciiService {

    private final BaciiRepository baciiRepository;

    @Override
    public BaciiResponse createBacii(BaciiRequest request) {
        if (baciiRepository.existsByGradeIgnoreCase(request.getGrade())) {
            throw new ConflictException("Bacii grade already exists");
        }
        Bacii saved = baciiRepository.save(request.toEntity());
        return saved.toResponse();
    }

    @Override
    public BaciiResponse getBaciiById(UUID baciiId) {
        Bacii bacii = baciiRepository.findById(baciiId).orElseThrow(
                () -> new NotFoundException("Bacii not " + baciiId + " found")
        );
        return bacii.toResponse();
    }

    @Override
    public PagedResponse<List<BaciiResponse>> getAllBaciis(int page, int size, String sortBy, Sort.Direction direction) {
        int zeroBased = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(zeroBased, size, Sort.by(direction, sortBy));
        Page<Bacii> pageBaciis = baciiRepository.findAll(pageable);

        List<BaciiResponse> items = pageBaciis
                .getContent()
                .stream()
                .map(Bacii::toResponse)
                .toList();

        return pageResponse(
                items,
                pageBaciis.getTotalElements(),
                page,
                size,
                pageBaciis.getTotalPages()
        );
    }

    @Override
    public BaciiResponse updateBaciiById(UUID baciiId, BaciiRequest request) {
        Bacii bacii = baciiRepository.findById(baciiId).orElseThrow(
                () -> new NotFoundException("Bacii not " + baciiId + " found")
        );

        if (baciiRepository.existsByGradeIgnoreCase(request.getGrade())) {
            throw new ConflictException("Bacii grade already exists");
        }

        Bacii saved = baciiRepository.save(request.toEntity(baciiId));
        return saved.toResponse();
    }

    @Override
    public void deleteBaciiById(UUID baciiId) {
        Bacii bacii = baciiRepository.findById(baciiId).orElseThrow(
                () -> new NotFoundException("Bacii not " + baciiId + " found")
        );
        baciiRepository.deleteById(bacii.getBaciiId());
    }
}

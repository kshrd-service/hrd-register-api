package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.BaciiRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.BaciiResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface BaciiService {

    BaciiResponse createBacii(BaciiRequest request);

    BaciiResponse getBaciiById(UUID baciiId);

    PagedResponse<List<BaciiResponse>> getAllBaciis(int page, int size, String sortBy, Sort.Direction direction);

    BaciiResponse updateBaciiById(UUID baciiId, BaciiRequest request);

    void deleteBaciiById(UUID baciiId);
}

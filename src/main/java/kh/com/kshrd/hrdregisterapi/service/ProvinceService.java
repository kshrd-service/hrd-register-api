package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.ProvinceRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.ProvinceResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface ProvinceService {

    ProvinceResponse createProvince(ProvinceRequest request);

    ProvinceResponse getProvinceById(UUID provinceId);

    PagedResponse<List<ProvinceResponse>> getAllProvinces(int page, int size, String sortBy, Sort.Direction direction);

    ProvinceResponse updateProvinceById(UUID provinceId, ProvinceRequest request);

    void deleteProvinceById(UUID provinceId);
}

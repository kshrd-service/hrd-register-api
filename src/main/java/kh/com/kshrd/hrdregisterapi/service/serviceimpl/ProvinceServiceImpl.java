package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.ProvinceRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.ProvinceResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.Province;
import kh.com.kshrd.hrdregisterapi.repository.ProvinceRepository;
import kh.com.kshrd.hrdregisterapi.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.pageResponse;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Override
    public ProvinceResponse createProvince(ProvinceRequest request) {
        if (provinceRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("Province name already exists");
        }
        Province saved = provinceRepository.save(request.toEntity());
        return saved.toResponse();
    }

    @Override
    public ProvinceResponse getProvinceById(UUID provinceId) {
        Province province = provinceRepository.findById(provinceId).orElseThrow(
                () -> new NotFoundException("Province not " + provinceId + " found")
        );
        return province.toResponse();
    }

    @Override
    public PagedResponse<List<ProvinceResponse>> getAllProvinces(int page, int size, String sortBy, Sort.Direction direction) {
        int zeroBased = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(zeroBased, size, Sort.by(direction, sortBy));
        Page<Province> pageProvinces = provinceRepository.findAll(pageable);

        List<ProvinceResponse> items = pageProvinces
                .getContent()
                .stream()
                .map(Province::toResponse)
                .toList();

        return pageResponse(
                items,
                pageProvinces.getTotalElements(),
                page,
                size,
                pageProvinces.getTotalPages()
        );
    }

    @Override
    public ProvinceResponse updateProvinceById(UUID provinceId, ProvinceRequest request) {
        Province province = provinceRepository.findById(provinceId).orElseThrow(
                () -> new NotFoundException("Province not " + provinceId + " found")
        );

        if (provinceRepository.existsByNameIgnoreCase(province.getName())) {
            throw new ConflictException("Province name already exists");
        }

        Province saved = provinceRepository.save(request.toEntity(provinceId));
        return saved.toResponse();
    }

    @Override
    public void deleteProvinceById(UUID provinceId) {
        Province province = provinceRepository.findById(provinceId).orElseThrow(
                () -> new NotFoundException("Province not " + provinceId + " found")
        );
        provinceRepository.deleteById(province.getProvinceId());
    }
}

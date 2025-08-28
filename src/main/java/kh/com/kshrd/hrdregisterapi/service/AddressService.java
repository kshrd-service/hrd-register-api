package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.request.AddressRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.AddressResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponse createAddress(AddressRequest request);

    AddressResponse getAddressById(UUID addressId);

    PagedResponse<List<AddressResponse>> getAllAddresses(int page, int size, String sortBy, Sort.Direction direction);

    AddressResponse updateAddressById(UUID addressId, AddressRequest request);

    void deleteAddressById(UUID addressId);
}

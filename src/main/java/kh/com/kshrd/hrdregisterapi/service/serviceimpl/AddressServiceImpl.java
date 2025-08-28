package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.AddressRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.AddressResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.Address;
import kh.com.kshrd.hrdregisterapi.repository.AddressRepository;
import kh.com.kshrd.hrdregisterapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.pageResponse;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public AddressResponse createAddress(AddressRequest request) {
        if (addressRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("Address name already exists");
        }
        Address saved = addressRepository.save(request.toEntity());
        return saved.toResponse();
    }

    @Override
    public AddressResponse getAddressById(UUID addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new NotFoundException("Address not " + addressId + " found")
        );
        return address.toResponse();
    }

    @Override
    public PagedResponse<List<AddressResponse>> getAllAddresses(int page, int size, String sortBy, Sort.Direction direction) {
        int zeroBased = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(zeroBased, size, Sort.by(direction, sortBy));
        Page<Address> pageSubjects = addressRepository.findAll(pageable);

        List<AddressResponse> items = pageSubjects
                .getContent()
                .stream()
                .map(Address::toResponse)
                .toList();

        return pageResponse(
                items,
                pageSubjects.getTotalElements(),
                page,
                size,
                pageSubjects.getTotalPages()
        );
    }

    @Override
    public AddressResponse updateAddressById(UUID addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new NotFoundException("Address not " + addressId + " found")
        );

        if (addressRepository.existsByNameIgnoreCase(address.getName())) {
            throw new ConflictException("Address name already exists");
        }

        Address saved = addressRepository.save(request.toEntity(addressId));
        return saved.toResponse();
    }

    @Override
    public void deleteAddressById(UUID addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new NotFoundException("Address not " + addressId + " found")
        );
        addressRepository.deleteById(address.getAddressId());
    }
}

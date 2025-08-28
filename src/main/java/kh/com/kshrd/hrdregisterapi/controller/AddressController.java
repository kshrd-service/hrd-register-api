package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kh.com.kshrd.hrdregisterapi.model.dto.request.AddressRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.AddressResponse;
import kh.com.kshrd.hrdregisterapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @Operation(
            summary = "Create address",
            description = "Creates a new address for the current user (or target entity depending on your domain).",
            tags = {"Address"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<AddressResponse>> createAddress(
            @RequestBody @Valid AddressRequest request
    ) {
        return buildResponse(
                "Address created successfully",
                addressService.createAddress(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{address-id}")
    @Operation(
            summary = "Get address by ID",
            description = "Returns a single address by its identifier.",
            tags = {"Address"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<APIResponse<AddressResponse>> getAddressById(
            @PathVariable("address-id") UUID addressId
    ) {
        return buildResponse(
                "Address retrieved successfully",
                addressService.getAddressById(addressId),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(
            summary = "List addresses (paged)",
            description = "Returns paginated addresses. Use query params to control pagination and sorting.",
            tags = {"Address"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponse<PagedResponse<List<AddressResponse>>>> getAllAddresses(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "addressId", required = false) String sortBy,
            @RequestParam(defaultValue = "DESC", required = false) Sort.Direction direction
    ) {
        return buildResponse(
                "Addresses retrieved successfully",
                addressService.getAllAddresses(page, size, sortBy, direction),
                HttpStatus.OK
        );
    }

    @PutMapping("/{address-id}")
    @Operation(
            summary = "Update address",
            description = "Fully updates an address by ID.",
            tags = {"Address"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., duplicate unique field)")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<AddressResponse>> updateAddressById(
            @PathVariable("address-id") UUID addressId,
            @RequestBody @Valid AddressRequest request
    ) {
        return buildResponse(
                "Address updated successfully",
                addressService.updateAddressById(addressId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{address-id}")
    @Operation(
            summary = "Delete address",
            description = "Deletes an address by ID.",
            tags = {"Address"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> deleteAddressById(
            @PathVariable("address-id") UUID addressId
    ) {
        addressService.deleteAddressById(addressId);
        return buildResponse(
                "Address deleted successfully",
                null,
                HttpStatus.NO_CONTENT
        );
    }
}

package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kh.com.kshrd.hrdregisterapi.model.dto.request.ProvinceRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.ProvinceResponse;
import kh.com.kshrd.hrdregisterapi.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    @PostMapping
    @Operation(
            summary = "Create province",
            description = "Creates a new province record.",
            tags = {"Province"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Province created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<ProvinceResponse>> createProvince(
            @RequestBody @Valid ProvinceRequest request
    ) {
        return buildResponse(
                "Province created successfully",
                provinceService.createProvince(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{province-id}")
    @Operation(
            summary = "Get province by ID",
            description = "Returns a single province by its identifier.",
            tags = {"Province"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Province retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Province not found")
    })
    public ResponseEntity<APIResponse<ProvinceResponse>> getProvinceById(
            @PathVariable("province-id") UUID provinceId
    ) {
        return buildResponse(
                "Province retrieved successfully",
                provinceService.getProvinceById(provinceId),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(
            summary = "List provinces (paged)",
            description = "Returns paginated provinces. Use query params to control pagination and sorting.",
            tags = {"Province"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provinces retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponse<PagedResponse<List<ProvinceResponse>>>> getAllProvinces(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "provinceId", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        return buildResponse(
                "Provinces retrieved successfully",
                provinceService.getAllProvinces(page, size, sortBy, direction),
                HttpStatus.OK
        );
    }

    @PutMapping("/{province-id}")
    @Operation(
            summary = "Update province",
            description = "Fully updates a province by ID.",
            tags = {"Province"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Province updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Province not found"),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., duplicate name)")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<ProvinceResponse>> updateProvinceById(
            @PathVariable("province-id") UUID provinceId,
            @RequestBody @Valid ProvinceRequest request
    ) {
        return buildResponse(
                "Province updated successfully",
                provinceService.updateProvinceById(provinceId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{province-id}")
    @Operation(
            summary = "Delete province",
            description = "Deletes a province by ID.",
            tags = {"Province"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Province deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Province not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> deleteProvinceById(
            @PathVariable("province-id") UUID provinceId
    ) {
        provinceService.deleteProvinceById(provinceId);
        return buildResponse(
                "Province deleted successfully",
                null,
                HttpStatus.OK
        );
    }
}

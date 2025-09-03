package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kh.com.kshrd.hrdregisterapi.model.dto.request.UniversityRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UniversityResponse;
import kh.com.kshrd.hrdregisterapi.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @PostMapping
    @Operation(
            summary = "Create university",
            description = "Creates a new university record.",
            tags = {"University"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "University created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<UniversityResponse>> createUniversity(
            @RequestBody @Valid UniversityRequest request
    ) {
        return buildResponse(
                "University created successfully",
                universityService.createUniversity(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{university-id}")
    @Operation(
            summary = "Get university by ID",
            description = "Returns a single university by its identifier.",
            tags = {"University"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "University retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "University not found")
    })
    public ResponseEntity<APIResponse<UniversityResponse>> getUniversityById(
            @PathVariable("university-id") UUID universityId
    ) {
        return buildResponse(
                "University retrieved successfully",
                universityService.getUniversityById(universityId),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(
            summary = "List universities (paged)",
            description = "Returns paginated universities. Use query params to control pagination and sorting.",
            tags = {"University"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Universities retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponse<PagedResponse<List<UniversityResponse>>>> getAllUniversities(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "universityId", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        return buildResponse(
                "Universities retrieved successfully",
                universityService.getAllUniversities(page, size, sortBy, direction),
                HttpStatus.OK
        );
    }

    @PutMapping("/{university-id}")
    @Operation(
            summary = "Update university",
            description = "Fully updates a university record by ID.",
            tags = {"University"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "University updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "University not found"),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., duplicate abbreviation)")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<UniversityResponse>> updateUniversityById(
            @PathVariable("university-id") UUID universityId,
            @RequestBody @Valid UniversityRequest request
    ) {
        return buildResponse(
                "University updated successfully",
                universityService.updateUniversityById(universityId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{university-id}")
    @Operation(
            summary = "Delete university",
            description = "Deletes a university record by ID.",
            tags = {"University"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "University deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "University not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> deleteUniversityById(
            @PathVariable("university-id") UUID universityId
    ) {
        universityService.deleteUniversityById(universityId);
        return buildResponse(
                "University deleted successfully",
                null,
                HttpStatus.OK
        );
    }
}

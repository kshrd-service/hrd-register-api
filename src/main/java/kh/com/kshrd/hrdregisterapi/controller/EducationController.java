package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kh.com.kshrd.hrdregisterapi.model.dto.request.EducationRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.EducationResponse;
import kh.com.kshrd.hrdregisterapi.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    @Operation(
            summary = "Create education",
            description = "Creates a new education record for the current user (or target entity depending on your domain).",
            tags = {"Education"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Education created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<EducationResponse>> createEducation(
            @RequestBody @Valid EducationRequest request
    ) {
        return buildResponse(
                "Education created successfully",
                educationService.createEducation(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{education-id}")
    @Operation(
            summary = "Get education by ID",
            description = "Returns a single education record by its identifier.",
            tags = {"Education"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Education retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Education not found")
    })
    public ResponseEntity<APIResponse<EducationResponse>> getEducationById(
            @PathVariable("education-id") UUID educationId
    ) {
        return buildResponse(
                "Education retrieved successfully",
                educationService.getEducationById(educationId),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(
            summary = "List educations (paged)",
            description = "Returns paginated education records. Use query params to control pagination and sorting.",
            tags = {"Education"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Educations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponse<PagedResponse<List<EducationResponse>>>> getAllEducations(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "educationId", required = false) String sortBy,
            @RequestParam(defaultValue = "DESC", required = false) Sort.Direction direction
    ) {
        return buildResponse(
                "Educations retrieved successfully",
                educationService.getAllEducations(page, size, sortBy, direction),
                HttpStatus.OK
        );
    }

    @PutMapping("/{education-id}")
    @Operation(
            summary = "Update education",
            description = "Fully updates an education record by ID.",
            tags = {"Education"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Education updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Education not found"),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., duplicate unique field)")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<EducationResponse>> updateEducationById(
            @PathVariable("education-id") UUID educationId,
            @RequestBody @Valid EducationRequest request
    ) {
        return buildResponse(
                "Education updated successfully",
                educationService.updateEducationById(educationId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{education-id}")
    @Operation(
            summary = "Delete education",
            description = "Deletes an education record by ID.",
            tags = {"Education"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Education deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Education not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> deleteEducationById(
            @PathVariable("education-id") UUID educationId
    ) {
        educationService.deleteEducationById(educationId);
        return buildResponse(
                "Education deleted successfully",
                null,
                HttpStatus.NO_CONTENT
        );
    }
}

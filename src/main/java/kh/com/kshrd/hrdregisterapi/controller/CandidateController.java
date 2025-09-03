package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kh.com.kshrd.hrdregisterapi.model.dto.request.CandidateRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    @Operation(
            summary = "Register candidate",
            description = "Creates a new candidate along with initial payment placeholder and related references.",
            tags = {"Candidate"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Candidate registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Email or phone already exists")
    })
    public ResponseEntity<APIResponse<CandidateResponse>> registerCandidate(
            @RequestBody @Valid CandidateRequest request
    ) throws Exception {
        return buildResponse(
                "Candidate registered successfully",
                candidateService.registerCandidate(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{candidate-id}")
    @Operation(
            summary = "Get candidate by ID",
            description = "Returns a candidate by its identifier.",
            tags = {"Candidate"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    public ResponseEntity<APIResponse<CandidateResponse>> getCandidateById(
            @PathVariable("candidate-id") UUID candidateId
    ) {
        return buildResponse(
                "Candidate retrieved successfully",
                candidateService.getCandidateById(candidateId),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(
            summary = "List candidates (paged)",
            description = "Returns paginated candidates. Use query params to control pagination and sorting.",
            tags = {"Candidate"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully")
    })
    public ResponseEntity<APIResponse<PagedResponse<List<CandidateResponse>>>> getAllCandidates(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "candidateId", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        return buildResponse(
                "Candidates retrieved successfully",
                candidateService.getAllCandidates(page, size, sortBy, direction),
                HttpStatus.OK
        );
    }

    @PutMapping("/{candidate-id}")
    @Operation(
            summary = "Update candidate",
            description = "Fully updates a candidate by ID.",
            tags = {"Candidate"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Candidate not found"),
            @ApiResponse(responseCode = "409", description = "Email or phone already exists")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<CandidateResponse>> updateCandidateById(
            @PathVariable("candidate-id") UUID candidateId,
            @RequestBody @Valid CandidateRequest request
    ) {
        return buildResponse(
                "Candidate updated successfully",
                candidateService.updateCandidateById(candidateId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{candidate-id}")
    @Operation(
            summary = "Delete candidate",
            description = "Deletes a candidate by ID.",
            tags = {"Candidate"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> deleteCandidateById(
            @PathVariable("candidate-id") UUID candidateId
    ) {
        candidateService.deleteCandidateById(candidateId);
        return buildResponse(
                "Candidate deleted successfully",
                null,
                HttpStatus.OK
        );
    }

    @PostMapping("/{candidate-id}/application-form/resend")
    @Operation(
            summary = "Resend application form",
            description = "Resends the application form email to the candidate. Normally this is sent automatically after payment is marked PAID, but this endpoint allows manual resend by an authorized user.",
            tags = {"Candidate"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application form resent successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> resendApplicationForm(@PathVariable("candidate-id") UUID candidateId) {
        candidateService.resendApplicationForm(candidateId);
        return buildResponse(
                "Application form resent successfully",
                null,
                HttpStatus.OK
        );
    }

    @PostMapping("/{candidate-id}/donation-form/resend")
    @Operation(
            summary = "Resend donation form",
            description = "Resends the donation form email to the candidate. Normally this is sent during registration, but this endpoint allows manual resend by an authorized user.",
            tags = {"Candidate"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donation form resent successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> resendDonationForm(@PathVariable("candidate-id") UUID candidateId) throws Exception {
        candidateService.resendDonationForm(candidateId);
        return buildResponse(
                "Donation form resent successfully",
                null,
                HttpStatus.OK
        );
    }


}

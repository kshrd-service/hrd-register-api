package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kh.com.kshrd.hrdregisterapi.model.dto.request.GenerationRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.GenerationResponse;
import kh.com.kshrd.hrdregisterapi.service.GenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/generations")
@RequiredArgsConstructor
public class GenerationController {

    private final GenerationService generationService;

    @PostMapping
    @Operation(
            summary = "Create generation",
            description = "Creates a new generation record.",
            tags = {"Generation"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Generation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<GenerationResponse>> createGeneration(
            @RequestBody @Valid GenerationRequest request
    ) {
        return buildResponse(
                "Generation created successfully",
                generationService.createGeneration(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{generation-id}")
    @Operation(
            summary = "Get generation by ID",
            description = "Returns a single generation record by its identifier.",
            tags = {"Generation"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generation retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Generation not found")
    })
    public ResponseEntity<APIResponse<GenerationResponse>> getGenerationById(
            @PathVariable("generation-id") UUID generationId
    ) {
        return buildResponse(
                "Generation retrieved successfully",
                generationService.getGenerationById(generationId),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(
            summary = "List generations (paged)",
            description = "Returns paginated generation records. Use query params to control pagination and sorting.",
            tags = {"Generation"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponse<PagedResponse<List<GenerationResponse>>>> getAllGenerations(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "generationId", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        return buildResponse(
                "Generations retrieved successfully",
                generationService.getAllGenerations(page, size, sortBy, direction),
                HttpStatus.OK
        );
    }

    @PutMapping("/{generation-id}")
    @Operation(
            summary = "Update generation",
            description = "Fully updates a generation record by ID.",
            tags = {"Generation"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generation updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Generation not found"),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., duplicate unique field)")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<GenerationResponse>> updateGenerationById(
            @PathVariable("generation-id") UUID generationId,
            @RequestBody @Valid GenerationRequest request
    ) {
        return buildResponse(
                "Generation updated successfully",
                generationService.updateGenerationById(generationId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{generation-id}")
    @Operation(
            summary = "Delete generation",
            description = "Deletes a generation record by ID.",
            tags = {"Generation"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generation deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Generation not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> deleteGenerationById(
            @PathVariable("generation-id") UUID generationId
    ) {
        generationService.deleteGenerationById(generationId);
        return buildResponse(
                "Generation deleted successfully",
                null,
                HttpStatus.OK
        );
    }
}

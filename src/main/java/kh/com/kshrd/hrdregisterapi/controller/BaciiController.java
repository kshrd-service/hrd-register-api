package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kh.com.kshrd.hrdregisterapi.model.dto.request.BaciiRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.BaciiResponse;
import kh.com.kshrd.hrdregisterapi.service.BaciiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/baciis")
@RequiredArgsConstructor
public class BaciiController {

    private final BaciiService baciiService;

    @PostMapping
    @Operation(
            summary = "Create Bacii",
            description = "Creates a new Bacii record.",
            tags = {"Bacii"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bacii created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<BaciiResponse>> createBacii(
            @RequestBody @Valid BaciiRequest request
    ) {
        return buildResponse(
                "Bacii created successfully",
                baciiService.createBacii(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{bacii-id}")
    @Operation(
            summary = "Get Bacii by ID",
            description = "Returns a single Bacii record by its identifier.",
            tags = {"Bacii"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bacii retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Bacii not found")
    })
    public ResponseEntity<APIResponse<BaciiResponse>> getBaciiById(
            @PathVariable("bacii-id") UUID baciiId
    ) {
        return buildResponse(
                "Bacii retrieved successfully",
                baciiService.getBaciiById(baciiId),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(
            summary = "List Baciis (paged)",
            description = "Returns paginated Bacii records. Use query params to control pagination and sorting.",
            tags = {"Bacii"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Baciis retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponse<PagedResponse<List<BaciiResponse>>>> getAllBaciis(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "baciiId", required = false) String sortBy,
            @RequestParam(defaultValue = "DESC", required = false) Sort.Direction direction
    ) {
        return buildResponse(
                "Baciis retrieved successfully",
                baciiService.getAllBaciis(page, size, sortBy, direction),
                HttpStatus.OK
        );
    }

    @PutMapping("/{bacii-id}")
    @Operation(
            summary = "Update Bacii",
            description = "Fully updates a Bacii record by ID.",
            tags = {"Bacii"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bacii updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Bacii not found"),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., duplicate name)")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<BaciiResponse>> updateBaciiById(
            @PathVariable("bacii-id") UUID baciiId,
            @RequestBody @Valid BaciiRequest request
    ) {
        return buildResponse(
                "Bacii updated successfully",
                baciiService.updateBaciiById(baciiId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{bacii-id}")
    @Operation(
            summary = "Delete Bacii",
            description = "Deletes a Bacii record by ID.",
            tags = {"Bacii"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bacii deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Bacii not found")
    })
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<APIResponse<Void>> deleteBaciiById(
            @PathVariable("bacii-id") UUID baciiId
    ) {
        baciiService.deleteBaciiById(baciiId);
        return buildResponse(
                "Bacii deleted successfully",
                null,
                HttpStatus.NO_CONTENT
        );
    }
}

package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UserResponse;
import kh.com.kshrd.hrdregisterapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "hrd")
public class UserController {

    private final UserService userService;

    @GetMapping("/user-info")
    @Operation(
            summary = "Get current authenticated user info",
            description = "Returns the profile of the currently authenticated user. Requires a valid Bearer token.",
            tags = {"User"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User info retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden — token valid but lacks required permissions")
    })
    public ResponseEntity<APIResponse<UserResponse>> getUserInfo() {
        return buildResponse(
                "User info retrieved successfully",
                userService.getUserInfo(),
                HttpStatus.OK
        );
    }
}

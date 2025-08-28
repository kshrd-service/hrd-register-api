package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kh.com.kshrd.hrdregisterapi.model.dto.request.LoginRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.request.RegisterRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.TokenResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UserResponse;
import kh.com.kshrd.hrdregisterapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided registration details.",
            tags = {"Auth"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<APIResponse<UserResponse>> register(
            @RequestBody @Valid RegisterRequest request) {
        return buildResponse(
                "User registered successfully",
                authService.register(request),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticates a user with email and password, and returns access tokens.",
            tags = {"Auth"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<APIResponse<TokenResponse>> login(
            @RequestBody @Valid LoginRequest request) throws Exception {
        return buildResponse(
                "Login successful",
                authService.login(request),
                HttpStatus.OK
        );
    }
}

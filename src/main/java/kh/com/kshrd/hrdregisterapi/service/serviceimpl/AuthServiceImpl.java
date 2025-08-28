package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.BadRequestException;
import kh.com.kshrd.hrdregisterapi.exception.ConflictException;
import kh.com.kshrd.hrdregisterapi.exception.UnauthorizeException;
import kh.com.kshrd.hrdregisterapi.jwt.JwtComponent;
import kh.com.kshrd.hrdregisterapi.model.dto.request.LoginRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.request.RegisterRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.response.TokenResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UserResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.User;
import kh.com.kshrd.hrdregisterapi.repository.UserRepository;
import kh.com.kshrd.hrdregisterapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtComponent jwtComponent;

    @Transactional
    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(request.toEntity());
        return saved.toResponse();
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticate(request.getEmail(), request.getPassword());

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtComponent.generateToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    private Authentication authenticate(String email, String rawPassword) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, rawPassword)
            );
        } catch (DisabledException ex) {
            throw new UnauthorizeException("User account is disabled");
        } catch (BadCredentialsException ex) {
            throw new UnauthorizeException("Invalid email or password");
        }
    }
}

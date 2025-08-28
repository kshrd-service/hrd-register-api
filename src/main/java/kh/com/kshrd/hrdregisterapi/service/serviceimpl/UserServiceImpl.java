package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.exception.UnauthorizeException;
import kh.com.kshrd.hrdregisterapi.model.dto.response.UserResponse;
import kh.com.kshrd.hrdregisterapi.model.entity.User;
import kh.com.kshrd.hrdregisterapi.repository.UserRepository;
import kh.com.kshrd.hrdregisterapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new NotFoundException("")
        );
    }

    @Override
    public UserResponse getUserInfo() {
        UUID userId = getUserIdOfCurrentUser();
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("")
        ).toResponse();
    }

    @Override
    public UUID getUserIdOfCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizeException("No authenticated user");
        }

        User user = (User) auth.getPrincipal();
        return user.getUserId();
    }
}

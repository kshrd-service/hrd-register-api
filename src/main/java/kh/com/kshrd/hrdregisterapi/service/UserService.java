package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserResponse getUserInfo();

    UUID getUserIdOfCurrentUser();
}

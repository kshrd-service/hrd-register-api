package kh.com.kshrd.hrdregisterapi.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private UUID userId;
    private String fullName;
    private String email;
    private String profile;
    private boolean isActive;
    private LocalDateTime createdAt;
}


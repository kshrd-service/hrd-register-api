package kh.com.kshrd.hrdregisterapi.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kh.com.kshrd.hrdregisterapi.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotNull
    @NotBlank
    private String fullName;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, include uppercase, lowercase, number, and special character"
    )
    private String password;

    private String profile;

    public User toEntity() {
        return User.builder()
                .fullName(this.fullName)
                .email(this.email)
                .password(this.password)
                .profile(this.profile)
                .build();
    }

}

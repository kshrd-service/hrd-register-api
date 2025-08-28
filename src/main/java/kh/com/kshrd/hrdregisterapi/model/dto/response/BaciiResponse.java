package kh.com.kshrd.hrdregisterapi.model.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaciiResponse {
    private UUID baciiId;
    private String grade;
}

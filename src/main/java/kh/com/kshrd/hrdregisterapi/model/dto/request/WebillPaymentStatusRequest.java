package kh.com.kshrd.hrdregisterapi.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebillPaymentStatusRequest {

    @JsonProperty("bill_no")
    private List<String> billNo;

}

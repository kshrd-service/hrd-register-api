package kh.com.kshrd.hrdregisterapi.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebillQuickBillRequest {

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("issue_datetime")
    private String issueDatetime;

    @JsonProperty("payment_term")
    private String paymentTerm;

    @JsonProperty("parent_account_no")
    private String parentAccountNo;

    private String amount;
    private String remark;

}

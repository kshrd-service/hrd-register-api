package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.kshrd.hrdregisterapi.exception.BadRequestException;
import kh.com.kshrd.hrdregisterapi.exception.NotFoundException;
import kh.com.kshrd.hrdregisterapi.exception.ServerErrorException;
import kh.com.kshrd.hrdregisterapi.model.dto.request.WebillAuthRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.request.WebillPaymentStatusRequest;
import kh.com.kshrd.hrdregisterapi.model.dto.request.WebillQuickBillRequest;
import kh.com.kshrd.hrdregisterapi.model.enums.Currency;
import kh.com.kshrd.hrdregisterapi.service.WebillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebillServiceImpl implements WebillService {

    @Value("${webill.URL}")
    private String url;

    @Value("${webill.client-id}")
    private String clientId;

    @Value("${webill.client-secret}")
    private String clientSecret;

    @Value("${webill.account-name}")
    private String accountName;

    @Value("${webill.account-no}")
    private String accountNo;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Override
    public String requestAccessToken() throws JsonProcessingException {

        WebillAuthRequest webillAuthRequest = WebillAuthRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        String json = restClient.post()
                .uri(url + "/api/wbi/client/v1/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(webillAuthRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    String errorBody = StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8);
                    if (res.getStatusCode().is4xxClientError()) {
                        throw new BadRequestException("Client error " + res.getStatusCode() + ": " + errorBody);
                    } else if (res.getStatusCode().is5xxServerError()) {
                        throw new ServerErrorException("Server error " + res.getStatusCode() + ": " + errorBody);
                    } else {
                        throw new RuntimeException("HTTP " + res.getStatusCode() + ": " + errorBody);
                    }
                })
                .body(String.class);

        JsonNode root = objectMapper.readTree(json);
        String accessToken = root.path("data").path("access_token").asText(null);

        if (accessToken == null || accessToken.isBlank()) {
            throw new NotFoundException("Access token not found in response: " + root);
        }

        return accessToken;
    }

    @Override
    public JsonNode createQuickBill() throws JsonProcessingException {

        String accessToken = requestAccessToken();

        String issueDatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime writingTest = LocalDateTime.of(2025, 12, 3, 0, 0);

        long daysBetween = ChronoUnit.DAYS.between(today, writingTest);
        String paymentTerm = String.valueOf(daysBetween);

        WebillQuickBillRequest webillQuickBillRequest = WebillQuickBillRequest.builder()
                .accountName(accountName)
                .paymentType("1")
                .currencyCode(String.valueOf(Currency.USD))
                .issueDatetime(issueDatetime)
                .paymentTerm(paymentTerm)
                .parentAccountNo(accountNo)
                .amount("5")
                .remark("Donation For Korea Software HRD Center")
                .build();

        String json = restClient.post()
                .uri(url + "/api/wbi/client/v1/quick-bills")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(webillQuickBillRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    String errorBody = StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8);
                    if (res.getStatusCode().is4xxClientError()) {
                        throw new BadRequestException("Client error " + res.getStatusCode() + ": " + errorBody);
                    } else if (res.getStatusCode().is5xxServerError()) {
                        throw new ServerErrorException("Server error " + res.getStatusCode() + ": " + errorBody);
                    } else {
                        throw new RuntimeException("HTTP " + res.getStatusCode() + ": " + errorBody);
                    }
                })
                .body(String.class);

        return objectMapper.readTree(json);
    }

    @Override
    public JsonNode checkPaymentStatus(String billNo) throws JsonProcessingException {
        String accessToken = requestAccessToken();

        WebillPaymentStatusRequest request = WebillPaymentStatusRequest.builder()
                .billNo(List.of(billNo))
                .build();

        String json = restClient.post()
                .uri(url + "/api/wbi/client/v1/payments/check-status")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    String errorBody = StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8);
                    if (res.getStatusCode().is4xxClientError()) {
                        throw new BadRequestException("Client error " + res.getStatusCode() + ": " + errorBody);
                    } else if (res.getStatusCode().is5xxServerError()) {
                        throw new ServerErrorException("Server error " + res.getStatusCode() + ": " + errorBody);
                    } else {
                        throw new RuntimeException("HTTP " + res.getStatusCode() + ": " + errorBody);
                    }
                })
                .body(String.class);

        return objectMapper.readTree(json);
    }

}

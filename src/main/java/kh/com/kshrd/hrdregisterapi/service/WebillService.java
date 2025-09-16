package kh.com.kshrd.hrdregisterapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface WebillService {

    String requestAccessToken() throws JsonProcessingException;

    JsonNode createQuickBill() throws JsonProcessingException;

    JsonNode checkPaymentStatus(String billNo) throws JsonProcessingException;

}

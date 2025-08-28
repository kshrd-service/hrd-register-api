package kh.com.kshrd.hrdregisterapi.utils;

import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PagedResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.PaginationInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ResponseUtil {

    public static <T> ResponseEntity<APIResponse<T>> buildResponse(String message, T payload, HttpStatus status) {
        APIResponse<T> response = new APIResponse<>(message, payload, status, Instant.now());
        return ResponseEntity.status(status).body(response);
    }

    public static <T> PagedResponse<T> pageResponse(T content, Long totalCount, Integer page, Integer size, Integer totalPages) {
        PaginationInfo paginationInfo = new PaginationInfo(totalCount, page, size, totalPages);
        return new PagedResponse<>(content, paginationInfo);
    }

}

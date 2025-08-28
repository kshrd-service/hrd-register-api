package kh.com.kshrd.hrdregisterapi.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationInfo {
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}

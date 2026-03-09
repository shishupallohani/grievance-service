package com.assist.grievance.data.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillEntryPageData {
    private List<BillEntryDto> content;
    private boolean last;
    private boolean first;
    private boolean empty;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private int numberOfElements;
}

package com.example.countryinfoapi.model.request;

import com.example.countryinfoapi.model.SortColumn;
import com.example.countryinfoapi.model.SortOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationInfo {
    @Value("${paginationInfo.pageNo}")
    private Integer pageNo;
    @Value("${paginationInfo.pageSize}")
    private Integer pageSize;
    private SortOrder sortDir;
    private SortColumn sortCol;
}

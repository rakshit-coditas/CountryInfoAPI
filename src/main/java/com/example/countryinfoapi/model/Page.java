package com.example.countryinfoapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Page<T> {
    private int pageNo;
    private int pageSize;
    private long totalRecords;
    private List<T> content;
}

package com.example.countryinfoapi.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CountryFilterRequest {
    private CountryFilters countryFilters;
    private PaginationInfo paginationInfo;
}

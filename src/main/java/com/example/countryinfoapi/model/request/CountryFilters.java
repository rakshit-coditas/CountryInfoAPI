package com.example.countryinfoapi.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryFilters {
    private List<String> languages;
    @Value("${countryFilters.minArea}")
    private Double minArea;
    @Value("${countryFilters.maxArea}")
    private Double maxArea;
    @Value("${countryFilters.minPopulation}")
    private Integer minPopulation;
    @Value("${countryFilters.maxPopulation}")
    private Integer maxPopulation;
    public static String filterColumn="?fields=languages,name,area,population";
}

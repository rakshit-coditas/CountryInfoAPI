package com.example.countryinfoapi.service.impl;

import com.example.countryinfoapi.exception.CountryNotFoundException;
import com.example.countryinfoapi.exception.ValidationException;
import com.example.countryinfoapi.model.Country;
import com.example.countryinfoapi.model.Page;
import com.example.countryinfoapi.model.SortColumn;
import com.example.countryinfoapi.model.SortOrder;
import com.example.countryinfoapi.model.request.CountryFilterRequest;
import com.example.countryinfoapi.model.request.CountryFilters;
import com.example.countryinfoapi.model.request.PaginationInfo;
import com.example.countryinfoapi.service.CountryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service public class CountryServiceImpl implements CountryService {

    private WebClient webClient;

    @Value("${countries.baseURL}")
    private String baseUrl;
    public String getCountryInfo(String countryName) {
        webClient = WebClient.create(baseUrl+"/name");
        try {
            return webClient.get().uri("/{countryName}", countryName).retrieve().bodyToMono(String.class).block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new CountryNotFoundException(countryName);
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Error: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error: " + ex.getMessage());
        }
    }

    public List<Country> getAllCountryInfo(String filterColumn) {
        webClient =
            WebClient.create(baseUrl+"/all" + ((filterColumn != null) ? filterColumn : ""));
        try {
            return webClient.get().retrieve().bodyToFlux(Country.class)
                .collectList()
                .block();
        } catch (WebClientResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error: " + ex.getMessage());
        }
    }

    public Page<Country> getAllCountryInfoWithFilters(final CountryFilterRequest countryFilterRequest) {
        try {
            validateCountryFilters(countryFilterRequest);
            List<Country> allCountries = getAllCountryInfo(CountryFilters.filterColumn);
            return filterCountries(allCountries, countryFilterRequest);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    // Custom validation logic
    private void validateCountryFilters(final CountryFilterRequest countryFilterRequest) {
        if (countryFilterRequest.getCountryFilters() == null) {
            throw new IllegalArgumentException("custom filters is mandatory");
        }
        if (countryFilterRequest.getPaginationInfo() == null) {
            throw new IllegalArgumentException("pagination info is mandatory");
        }
        if (countryFilterRequest.getPaginationInfo() != null) {
            PaginationInfo paginationInfo = countryFilterRequest.getPaginationInfo();

            if (paginationInfo.getPageNo() == null || paginationInfo.getPageNo() < 0) {
                throw new ValidationException("Page number must be greater than 0");
            }

            if (paginationInfo.getPageSize() == null || paginationInfo.getPageSize() <= 0) {
                throw new ValidationException("Page size must be greater than 0");
            }
        }
        if (countryFilterRequest.getCountryFilters() != null) {
            CountryFilters filters = countryFilterRequest.getCountryFilters();

            if (filters.getMinPopulation() != null && filters.getMaxPopulation() != null
                && filters.getMinPopulation() > filters.getMaxPopulation()) {
                throw new ValidationException("minPopulation must be less than or equal to maxPopulation");
            }

            if (filters.getMinArea() != null && filters.getMaxArea() != null
                && filters.getMinArea() > filters.getMaxArea()) {
                throw new ValidationException("minArea must be less than or equal to maxArea");
            }
        }
    }

    private Page<Country> filterCountries(List<Country> allCountries,
        final CountryFilterRequest countryFilterRequest) {
        CountryFilters filters = countryFilterRequest.getCountryFilters();
        //filters applied
        List<Country> filteredCountryList =
            allCountries.stream().filter(country -> isLanguageMatch(country, filters.getLanguages()))
                .filter(country -> isPopulationMatch(country, filters.getMinPopulation(), filters.getMaxPopulation()))
                .filter(country -> isAreaMatch(country, filters.getMinArea(), filters.getMaxArea()))
                .collect(Collectors.toList());
        // Sorting
        if (countryFilterRequest.getPaginationInfo().getSortCol() != null) {
            Comparator<Country> comparator = getComparator(countryFilterRequest.getPaginationInfo().getSortCol());
            if (countryFilterRequest.getPaginationInfo().getSortDir() == SortOrder.DESC) {
                comparator = comparator.reversed();
            }
            filteredCountryList.sort(comparator);
        }
        // pagination logic
        int startIndex = (countryFilterRequest.getPaginationInfo().getPageNo() - 1) * countryFilterRequest.getPaginationInfo().getPageSize();
        int endIndex = Math.min(startIndex + countryFilterRequest.getPaginationInfo().getPageSize(), filteredCountryList.size());

        if (startIndex>endIndex || startIndex < 0 || endIndex > filteredCountryList.size()) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        Page<Country> page = Page.<Country>builder().pageNo(countryFilterRequest.getPaginationInfo().getPageNo())
            .pageSize(countryFilterRequest.getPaginationInfo().getPageSize()).totalRecords(filteredCountryList.size())
            .content(filteredCountryList.subList(startIndex, endIndex)).build();
        return page;
    }

    private boolean isLanguageMatch(Country country, List<String> languages) {
        if (languages == null || languages.isEmpty()) {
            return true; // No language filter
        }
        for (String language : languages) {
            if (country.getLanguages() != null && country.getLanguages().containsValue(language)) {
                return true; // At least one language from the list matches
            }
        }
        return false; // No matching language found
    }

    private boolean isPopulationMatch(Country country, Integer minPopulation, Integer maxPopulation) {
        if (minPopulation != null && country.getPopulation() < minPopulation) {
            return false;
        }
        return maxPopulation == null || country.getPopulation() <= maxPopulation;
    }

    private boolean isAreaMatch(Country country, Double minArea, Double maxArea) {
        if (minArea != null && country.getArea() < minArea) {
            return false;
        }
        return maxArea == null || country.getArea() <= maxArea;
    }

    private Comparator<Country> getComparator(SortColumn sortBy) {
        switch (sortBy) {
            case POPULATION:
                return Comparator.comparing(Country::getPopulation);
            case AREA:
                return Comparator.comparing(Country::getArea);
            case NAME:// Assuming getName().getOfficial() returns the "official" name under the "name" field
                return Comparator.comparing(country -> country.getName().getOfficial());
            default:
                throw new IllegalArgumentException("Invalid sorting column: " + sortBy);
        }
    }
}

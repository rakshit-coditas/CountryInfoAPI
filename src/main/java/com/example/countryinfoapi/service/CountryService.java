package com.example.countryinfoapi.service;

import com.example.countryinfoapi.model.Country;
import com.example.countryinfoapi.model.Page;
import com.example.countryinfoapi.model.request.CountryFilterRequest;

import java.util.List;

public interface CountryService {
    public String getCountryInfo(String countryName) ;
    public List<Country> getAllCountryInfo(String filterColumn) ;
    public Page<Country> getAllCountryInfoWithFilters(final CountryFilterRequest countryFilterRequest) ;
}

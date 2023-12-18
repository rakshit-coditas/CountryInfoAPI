package com.example.countryinfoapi.resource;

import com.example.countryinfoapi.model.Country;
import com.example.countryinfoapi.model.Page;
import com.example.countryinfoapi.model.request.CountryFilterRequest;
import com.example.countryinfoapi.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    /*get country info by name */
    @GetMapping("/info/{countryName}") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCountryInfo(@PathVariable String countryName) {
        String countryInfo = countryService.getCountryInfo(countryName);
        if (countryInfo != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(countryInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body("{\"error\": \"Country not found\"}");
        }
    }

    @GetMapping("/info/all") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Country>> getAllCountryInfo() {
        List<Country> allCountryInfo = countryService.getAllCountryInfo(null);
        return ResponseEntity.ok(allCountryInfo);
    }

    @PostMapping("/filter") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Country>> filterAllHorses(@RequestBody final CountryFilterRequest countryFilterRequest) {
        Page<Country> allCountryInfo = countryService.getAllCountryInfoWithFilters(countryFilterRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(allCountryInfo);
    }
}

package com.example.countryinfoapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Country {
    private Name name;
    private List<String> tld;
    private String cca2;
    private String ccn3;
    private String cca3;
    private String cioc;
    private Boolean independent;
    private String status;
    private Boolean unMember;
    private Map<String, Currency> currencies;
    private Idd idd;
    private List<String> capital;
    private List<String> altSpellings;
    private String region;
    private String subregion;
    private Map<String, String> languages;
    private Map<String, Map<String, String>> translations;
    private List<Double> latlng;
    private Boolean landlocked;
    private List<String> borders;
    private Double area;
    private Map<String, Map<String, String>> demonyms;
    private List<String> callingCodes;
    private String flag;
    private Map<String, String> maps;
    private Integer population;
    private Map<String, Double> gini;
    private String fifa;
    private Car car;
    private List<String> timezones;
    private List<String> continents;
    private Flag flags;
    private Flag coatOfArms;
    private String startOfWeek;
    private CapitalInformation capitalInfo;
    private Map<String, String> postalCode;

}

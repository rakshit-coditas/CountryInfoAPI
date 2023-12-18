package com.example.countryinfoapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostalCode {
    private String format;
    private String regex;
}

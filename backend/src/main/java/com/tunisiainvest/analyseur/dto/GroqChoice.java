package com.tunisiainvest.analyseur.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroqChoice {
    private GroqMessage message;
}
package com.tunisiainvest.analyseur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AnalyseRequest {

    // Section 1 : Projet
    @NotBlank
    private String secteur;

    @NotBlank
    private String region;

    @NotBlank
    private String description;

    @NotBlank
    private String stadeProjet;

    // Section 2 : Finances
    @NotNull
    @Positive
    private Double budgetInitial;

    @NotBlank
    private String sourceFinancement;

    @NotNull
    @Positive
    private Double chiffreAffaires1An;

    @NotBlank
    private String delaiRoi;

    // Section 3 : Contexte
    @NotBlank
    private String experiencePorteur;

    @NotBlank
    private String concurrents;

    @NotBlank
    private String avantagesFiscaux;

    @NotBlank
    private String zoneFranche;
}
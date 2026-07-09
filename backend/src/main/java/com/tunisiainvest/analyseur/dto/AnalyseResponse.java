package com.tunisiainvest.analyseur.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyseResponse {

    private Long id;

    private String secteur;
    private String region;

    private Integer scoreFinancier;
    private Integer scoreMarche;
    private Integer scoreOperationnel;
    private Integer scoreReglementaire;
    private Integer scoreProfil;
    private Integer scoreGlobal;

    private String analyseFinanciere;
    private String analyseMarche;
    private String analyseOperationnelle;
    private String analyseReglementaire;
    private String analyseProfil;

    private String risques;
    private String recommandations;

    private LocalDateTime dateCreation;
}
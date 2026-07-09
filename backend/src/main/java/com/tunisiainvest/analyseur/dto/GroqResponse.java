package com.tunisiainvest.analyseur.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroqResponse {

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

    private List<String> risques;
    private List<String> recommandations;

}
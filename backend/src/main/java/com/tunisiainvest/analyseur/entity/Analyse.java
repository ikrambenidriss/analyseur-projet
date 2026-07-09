package com.tunisiainvest.analyseur.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "analyses")
@Data
public class Analyse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Section 1 : Projet
    private String secteur;
    private String region;

    @Column(length = 2000)
    private String description;

    private String stadeProjet;

    // Section 2 : Finances
    private Double budgetInitial;
    private String sourceFinancement;
    private Double chiffreAffaires1An;
    private String delaiRoi;

    // Section 3 : Contexte
    private String experiencePorteur;
    private String concurrents;
    private String avantagesFiscaux;
    private String zoneFranche;

    // Scores IA
    private Integer scoreFinancier;
    private Integer scoreMarche;
    private Integer scoreOperationnel;
    private Integer scoreReglementaire;
    private Integer scoreProfil;
    private Integer scoreGlobal;

    // Analyses narratives
    @Column(length = 2000)
    private String analyseFinanciere;

    @Column(length = 2000)
    private String analyseMarche;

    @Column(length = 2000)
    private String analyseOperationnelle;

    @Column(length = 2000)
    private String analyseReglementaire;

    @Column(length = 2000)
    private String analyseProfil;

    // Top 3 risques
    @Column(length = 3000)
    private String risques;

    // Top 3 recommandations
    @Column(length = 3000)
    private String recommandations;

    private LocalDateTime dateCreation;
}
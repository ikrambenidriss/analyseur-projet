package com.tunisiainvest.analyseur.service;

import com.tunisiainvest.analyseur.dto.AnalyseRequest;
import com.tunisiainvest.analyseur.dto.AnalyseResponse;
import com.tunisiainvest.analyseur.entity.Analyse;
import com.tunisiainvest.analyseur.repository.AnalyseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyseService {

    private final AnalyseRepository analyseRepository;
    private final GroqService groqService;

    public AnalyseService(AnalyseRepository analyseRepository, GroqService groqService) {
        this.analyseRepository = analyseRepository;
        this.groqService = groqService;
    }

    public AnalyseResponse lancerAnalyse(AnalyseRequest request) {
        Analyse analyse = new Analyse();

        analyse.setSecteur(request.getSecteur());
        analyse.setRegion(request.getRegion());
        analyse.setDescription(request.getDescription());
        analyse.setStadeProjet(request.getStadeProjet());

        analyse.setBudgetInitial(request.getBudgetInitial());
        analyse.setSourceFinancement(request.getSourceFinancement());
        analyse.setChiffreAffaires1An(request.getChiffreAffaires1An());
        analyse.setDelaiRoi(request.getDelaiRoi());

        analyse.setExperiencePorteur(request.getExperiencePorteur());
        analyse.setConcurrents(request.getConcurrents());
        analyse.setAvantagesFiscaux(request.getAvantagesFiscaux());
        analyse.setZoneFranche(request.getZoneFranche());

        groqService.remplirAnalyseIA(analyse, request);

        analyse.setDateCreation(LocalDateTime.now());

        Analyse saved = analyseRepository.save(analyse);

        return convertirEnResponse(saved);
    }

    public List<AnalyseResponse> recupererHistorique() {
        return analyseRepository.findAll()
                .stream()
                .map(this::convertirEnResponse)
                .collect(Collectors.toList());
    }

    private AnalyseResponse convertirEnResponse(Analyse analyse) {
        AnalyseResponse response = new AnalyseResponse();

        response.setId(analyse.getId());
        response.setSecteur(analyse.getSecteur());
        response.setRegion(analyse.getRegion());

        response.setScoreFinancier(analyse.getScoreFinancier());
        response.setScoreMarche(analyse.getScoreMarche());
        response.setScoreOperationnel(analyse.getScoreOperationnel());
        response.setScoreReglementaire(analyse.getScoreReglementaire());
        response.setScoreProfil(analyse.getScoreProfil());
        response.setScoreGlobal(analyse.getScoreGlobal());

        response.setAnalyseFinanciere(analyse.getAnalyseFinanciere());
        response.setAnalyseMarche(analyse.getAnalyseMarche());
        response.setAnalyseOperationnelle(analyse.getAnalyseOperationnelle());
        response.setAnalyseReglementaire(analyse.getAnalyseReglementaire());
        response.setAnalyseProfil(analyse.getAnalyseProfil());

        response.setRisques(analyse.getRisques());
        response.setRecommandations(analyse.getRecommandations());

        response.setDateCreation(analyse.getDateCreation());

        return response;
    }
}
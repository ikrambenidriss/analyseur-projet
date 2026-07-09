package com.tunisiainvest.analyseur.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunisiainvest.analyseur.dto.AnalyseRequest;
import com.tunisiainvest.analyseur.dto.GroqApiResponse;
import com.tunisiainvest.analyseur.dto.GroqResponse;
import com.tunisiainvest.analyseur.entity.Analyse;
import com.tunisiainvest.analyseur.exception.GroqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private static final Logger log = LoggerFactory.getLogger(GroqService.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String model;

    public GroqService(RestClient restClient,
                   ObjectMapper objectMapper) {
    this.restClient = restClient;
    this.objectMapper = objectMapper;
}

    public void remplirAnalyseIA(Analyse analyse, AnalyseRequest request) {

        log.info("Début de l'analyse IA pour secteur={} région={}", request.getSecteur(), request.getRegion());
        log.info("Configuration Groq chargée : url={}, modèle={}, cléPrésente={}",
                apiUrl, model, apiKey != null && !apiKey.isBlank());

        String prompt = construirePrompt(request);
        log.debug("Prompt envoyé à Groq : {}", prompt);

        try {
            String reponseGroq = appelerGroq(prompt);

            GroqApiResponse apiResponse =
                    objectMapper.readValue(reponseGroq, GroqApiResponse.class);

            String jsonAnalyse = apiResponse.getChoices()
                    .get(0)
                    .getMessage()
                    .getContent()
                    .trim();

            log.debug("JSON analyse reçu de Groq : {}", jsonAnalyse);

            GroqResponse analyseIA =
                    objectMapper.readValue(jsonAnalyse, GroqResponse.class);

            analyse.setScoreFinancier(analyseIA.getScoreFinancier());
            analyse.setScoreMarche(analyseIA.getScoreMarche());
            analyse.setScoreOperationnel(analyseIA.getScoreOperationnel());
            analyse.setScoreReglementaire(analyseIA.getScoreReglementaire());
            analyse.setScoreProfil(analyseIA.getScoreProfil());
            analyse.setScoreGlobal(analyseIA.getScoreGlobal());

            analyse.setAnalyseFinanciere(analyseIA.getAnalyseFinanciere());
            analyse.setAnalyseMarche(analyseIA.getAnalyseMarche());
            analyse.setAnalyseOperationnelle(analyseIA.getAnalyseOperationnelle());
            analyse.setAnalyseReglementaire(analyseIA.getAnalyseReglementaire());
            analyse.setAnalyseProfil(analyseIA.getAnalyseProfil());

            analyse.setRisques(String.join("\n", analyseIA.getRisques()));
            analyse.setRecommandations(String.join("\n", analyseIA.getRecommandations()));

            log.info("Analyse IA terminée avec succès. Score global={}", analyseIA.getScoreGlobal());

        } catch (Exception e) {
            log.error("Erreur lors de l'analyse du projet par Groq", e);
            throw new GroqException("Erreur lors de l'analyse du projet par Groq.", e);
        }
    }

    private String construirePrompt(AnalyseRequest request) {
        return """
                Tu es un expert tunisien en création d'entreprise.

                Analyse le projet suivant :

                Secteur : %s
                Région : %s
                Description : %s
                Stade du projet : %s

                Budget initial : %.2f TND
                Source de financement : %s
                Chiffre d'affaires prévu à 1 an : %.2f TND
                Délai de ROI : %s

                Expérience du porteur : %s
                Concurrence : %s
                Avantages fiscaux : %s
                Zone franche : %s

                Donne ton analyse UNIQUEMENT au format JSON.

                Le JSON doit être STRICTEMENT valide et avoir EXACTEMENT cette structure :

                {
                  "scoreFinancier": 0,
                  "scoreMarche": 0,
                  "scoreOperationnel": 0,
                  "scoreReglementaire": 0,
                  "scoreProfil": 0,
                  "scoreGlobal": 0,

                  "analyseFinanciere": "",
                  "analyseMarche": "",
                  "analyseOperationnelle": "",
                  "analyseReglementaire": "",
                  "analyseProfil": "",

                  "risques": [
                    "",
                    "",
                    ""
                  ],

                  "recommandations": [
                    "",
                    "",
                    ""
                  ]
                }

                Contraintes :

                - Les scores sont des nombres entiers compris entre 0 et 100.
                - Les analyses doivent être rédigées en français.
                - Les tableaux "risques" et "recommandations" doivent contenir exactement 3 éléments.
                - Les champs "analyse..." doivent contenir un paragraphe clair et concis.
                - N'ajoute aucun autre champ que ceux demandés.
                - Ne réponds avec aucun texte avant ou après le JSON.
                - Ne mets pas de balises Markdown.
                - Ne mets pas ```json.
                - Le résultat doit être un JSON valide pouvant être désérialisé directement par une application Java.
                """.formatted(
                request.getSecteur(),
                request.getRegion(),
                request.getDescription(),
                request.getStadeProjet(),
                request.getBudgetInitial(),
                request.getSourceFinancement(),
                request.getChiffreAffaires1An(),
                request.getDelaiRoi(),
                request.getExperiencePorteur(),
                request.getConcurrents(),
                request.getAvantagesFiscaux(),
                request.getZoneFranche()
        );
    }

    private String appelerGroq(String prompt) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                )
        );

        return restClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
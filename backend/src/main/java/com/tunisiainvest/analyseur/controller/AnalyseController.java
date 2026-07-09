package com.tunisiainvest.analyseur.controller;

import com.tunisiainvest.analyseur.dto.AnalyseRequest;
import com.tunisiainvest.analyseur.dto.AnalyseResponse;
import com.tunisiainvest.analyseur.service.AnalyseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyse")
public class AnalyseController {

    private final AnalyseService analyseService;

    public AnalyseController(AnalyseService analyseService) {
        this.analyseService = analyseService;
    }

    @PostMapping("/lancer")
    public AnalyseResponse lancerAnalyse(@Valid @RequestBody AnalyseRequest request) {
        return analyseService.lancerAnalyse(request);
    }

    @GetMapping("/historique")
    public List<AnalyseResponse> recupererHistorique() {
        return analyseService.recupererHistorique();
    }
}
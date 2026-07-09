package com.tunisiainvest.analyseur.repository;

import com.tunisiainvest.analyseur.entity.Analyse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyseRepository extends JpaRepository<Analyse, Long> {
}
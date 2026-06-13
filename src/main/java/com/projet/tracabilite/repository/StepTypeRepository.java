package com.projet.tracabilite.repository;

import com.projet.tracabilite.entity.StepType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository pour l'entité StepType (anciennement enum).
 * Permet la gestion dynamique des types d'étape en base de données.
 */
public interface StepTypeRepository extends JpaRepository<StepType, Long> {

    /** Recherche un type d'étape par son nom/code (ex: "CONDITIONNEMENT") */
    Optional<StepType> findByName(String name);
}

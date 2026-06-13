package com.projet.tracabilite.repository;

import com.projet.tracabilite.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository pour l'entité Status (anciennement enum).
 * Permet la gestion dynamique des statuts en base de données.
 */
public interface StatusRepository extends JpaRepository<Status, Long> {

    /** Recherche un statut par son nom/code (ex: "EN_COURS") */
    Optional<Status> findByName(String name);
}

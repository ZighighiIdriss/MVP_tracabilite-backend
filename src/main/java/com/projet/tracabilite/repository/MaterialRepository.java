package com.projet.tracabilite.repository;

import com.projet.tracabilite.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository pour l'entité Material.
 * Permet la gestion dynamique des matières premières en base de données.
 */
public interface MaterialRepository extends JpaRepository<Material, Long> {

    /** Recherche une matière par son nom/code (ex: "ROS") */
    Optional<Material> findByName(String name);
}

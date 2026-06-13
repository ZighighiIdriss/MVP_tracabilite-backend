package com.projet.tracabilite.repository;

import com.projet.tracabilite.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository pour l'entité ProductType (anciennement enum).
 * Permet la gestion dynamique des types de produit en base de données.
 */
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    /** Recherche un type de produit par son nom/code (ex: "FLO") */
    Optional<ProductType> findByName(String name);
}

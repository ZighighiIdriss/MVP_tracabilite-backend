package com.projet.tracabilite.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entité JPA de référence pour les types de produit.
 * Remplace l'ancien enum ProductType (FLO, BIO, CAR, HUI).
 * Les valeurs sont désormais gérées dynamiquement en base de données.
 */
@Entity
@Table(name = "product_types")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom/code du type de produit (ex: "FLO", "BIO", "CAR", "HUI") */
    @Column(unique = true, nullable = false)
    private String name;
}

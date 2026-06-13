package com.projet.tracabilite.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entité JPA de référence pour les statuts de produit.
 * Les valeurs sont désormais gérées dynamiquement en base de données.
 */
@Entity
@Table(name = "statuses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom/code du statut (ex: "EN_COURS", "TERMINE", "ANNULE") */
    @Column(unique = true, nullable = false)
    private String name;
}

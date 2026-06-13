package com.projet.tracabilite.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entité JPA de référence pour les types d'étapes de traçabilité.
 * Les valeurs sont désormais gérées dynamiquement en base de données.
 */
@Entity
@Table(name = "step_types")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom/code du type d'étape (ex: "RECEPTION_MATIERE", "CONDITIONNEMENT") */
    @Column(unique = true, nullable = false)
    private String name;
}

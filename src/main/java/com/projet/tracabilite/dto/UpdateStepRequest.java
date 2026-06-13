package com.projet.tracabilite.dto;

import lombok.*;

import java.util.Map;

/**
 * DTO de requête pour la mise à jour d'une étape de traçabilité.
 * Reçu via PUT /api/products/steps/{stepId}
 *
 * Tous les champs sont optionnels : seuls les champs fournis seront mis à jour.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStepRequest {

    /** Nouveau type d'étape (ID de la table step_types) */
    private Long typeId;

    /** Nouvelle description */
    private String description;

    /** Nouvelle localisation */
    private String location;

    /** Champs dynamiques (paires clé/valeur personnalisables) */
    private Map<String, Object> additionalInfo;
}

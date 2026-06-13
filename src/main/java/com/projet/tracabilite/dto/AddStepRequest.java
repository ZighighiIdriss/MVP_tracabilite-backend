package com.projet.tracabilite.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

/**
 * DTO de requête pour l'ajout d'une étape de traçabilité.
 * Reçu via POST /api/products/{id}/steps
 *
 * MODIFIÉ : le champ typeId (Long) a été remplacé par stepName (String)
 * pour permettre la création dynamique de types d'étape personnalisés.
 * Le backend résout ou crée le StepType correspondant automatiquement.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddStepRequest {

    // ========== Long typeId → String stepName ==========
    @NotBlank(message = "Le nom de l'étape est obligatoire")
    private String stepName;

    private java.time.LocalDateTime date;

    private String description;

    // ========== Champs dynamiques (paires clé/valeur personnalisables) ==========
    private Map<String, Object> additionalInfo;

    private String location;
}

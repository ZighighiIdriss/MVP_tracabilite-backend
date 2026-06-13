package com.projet.tracabilite.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Map;

/**
 * DTO de requête pour la mise à jour d'un produit existant.
 * Reçu via PUT /api/products/{id}
 *
 * Tous les champs sont optionnels : seuls les champs fournis seront mis à jour.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    /** Nouveau type de produit (ID de la table product_types) */
    private Long typeId;

    /** Nouvelle matière première (ID de la table materials) */
    private Long materialId;

    @Positive(message = "La quantité doit être positive")
    private Double quantity;

    /** Nouveau statut (ID de la table statuses) */
    private Long statusId;

    /** Nouveau statut par nom (find-or-create dynamique) — alternatif à statusId */
    private String statusName;

    /** Nouveau fournisseur (ID de la table suppliers) */
    private Long supplierId;

    /** Champs dynamiques (paires clé/valeur personnalisables) */
    private Map<String, Object> additionalInfo;
}

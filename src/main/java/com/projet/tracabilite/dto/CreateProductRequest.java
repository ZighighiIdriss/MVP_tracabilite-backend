package com.projet.tracabilite.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Map;

/**
 * DTO de requête pour la création d'un produit.
 * Reçu via POST /api/products
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    // ========== ProductType type → Long typeId ==========
    @NotNull(message = "Le type de produit est obligatoire")
    private Long typeId;

    // ========== Material material → Long materialId ==========
    @NotNull(message = "La matière est obligatoire")
    private Long materialId;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Double quantity;

    // ========== Supplier supplier → Long supplierId ==========
    @NotNull(message = "Le fournisseur est obligatoire")
    private Long supplierId;

    // ========== Date de collecte obligatoire ==========
    @NotNull(message = "La date de collecte est obligatoire")
    private java.time.LocalDateTime collectionDate;

    @NotNull(message = "L'étape initiale est obligatoire")
    private Long initialStepId;

    // ========== Champs dynamiques (paires clé/valeur personnalisables) ==========
    private Map<String, Object> additionalInfo;
}

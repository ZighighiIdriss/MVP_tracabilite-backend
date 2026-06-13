package com.projet.tracabilite.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO pour l'anonymisation et le transfert des données produit.
 *
 * MODIFIÉ : les champs type, material et currentStatus sont désormais
 * des String (le name de l'entité de référence) pour que le frontend
 * puisse afficher directement les libellés.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String productCode;

    // ========== ProductType type → String type (nom de l'entité) ==========
    private String type;

    // ========== Material material → String material (nom de l'entité) ==========
    private String material;

    private Double quantity;
    private LocalDateTime collectionDate;

    // ========== String currentStatus reste String mais provient de l'entité Status
    // ==========
    private String currentStatus;

    private String supplierCode;
    private String supplierName;
    private List<StepDTO> steps;

    // ========== Champs dynamiques (paires clé/valeur personnalisables) ==========
    private Map<String, Object> additionalInfo;
}

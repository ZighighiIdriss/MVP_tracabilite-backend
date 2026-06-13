package com.projet.tracabilite.dto;

import lombok.*;

/**
 * DTO de réponse pour les statistiques du dashboard.
 * Retourné via GET /api/dashboard
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {

    private long totalProducts;

    // Statuts actifs détaillés
    private long productsEnReception;
    private long productsEnTransformation;
    private long productsEnPurification;

    // Agrégé (utile pour l'affichage "X produits actifs")
    private long productsEnCours;

    // Terminés / Annulés
    private long productsTermines;
    private long productsAnnules;

    private long totalSteps;
}

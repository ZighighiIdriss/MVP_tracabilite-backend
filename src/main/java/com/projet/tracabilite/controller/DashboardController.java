package com.projet.tracabilite.controller;

import com.projet.tracabilite.dto.DashboardStatsDTO;
import com.projet.tracabilite.dto.ProductDTO;
import com.projet.tracabilite.entity.Product;
import com.projet.tracabilite.repository.ProductRepository;
import com.projet.tracabilite.repository.TraceabilityStepRepository;
import com.projet.tracabilite.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour le tableau de bord et les statistiques.
 * Écran : Dashboard / Consulter le Dashboard & Stats
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProductRepository productRepository;
    private final TraceabilityStepRepository stepRepository;
    private final ProductService productService;

    /**
     * Statuts considérés comme « inactifs » (règle métier portée par la couche
     * contrôleur).
     * Tout produit dont le statut n'est PAS dans cette liste est considéré comme
     * actif.
     */
    private static final List<String> INACTIVE_STATUSES = List.of("TERMINE", "ANNULE");

    /**
     * Récupère les statistiques globales du dashboard.
     * GET /api/dashboard
     */
    @GetMapping
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .totalProducts(productRepository.count())
                // Détail par étape active
                .productsEnReception(productRepository.countByCurrentStatusName("RECEPTION_MATIERE"))
                .productsEnTransformation(productRepository.countByCurrentStatusName("TRANSFORMATION"))
                .productsEnPurification(productRepository.countByCurrentStatusName("PURIFICATION_EXTRACTION"))
                // Agrégé : tous les produits actifs (= ni TERMINE, ni ANNULE)
                .productsEnCours(productRepository.countByCurrentStatusNameNotIn(INACTIVE_STATUSES))
                .productsTermines(productRepository.countByCurrentStatusName("TERMINE"))
                .productsAnnules(productRepository.countByCurrentStatusName("ANNULE"))
                .totalSteps(stepRepository.count())
                .build();

        return ResponseEntity.ok(stats);
    }

    /**
     * Récupère le résumé des produits récents.
     * GET /api/dashboard/recent-products
     */
    @GetMapping("/recent-products")
    public ResponseEntity<List<ProductDTO>> getRecentProducts() {
        List<Product> recentProducts = productRepository.findTop5ByOrderByIdDesc();

        List<ProductDTO> dtos = recentProducts.stream()
                .map(product -> productService.getProductById(product.getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}

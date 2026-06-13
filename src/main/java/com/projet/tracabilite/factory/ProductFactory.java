package com.projet.tracabilite.factory;

import com.projet.tracabilite.entity.Material;
import com.projet.tracabilite.entity.Product;
import com.projet.tracabilite.entity.ProductType;
import com.projet.tracabilite.entity.Status;
import com.projet.tracabilite.entity.Supplier;
import com.projet.tracabilite.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Factory responsable de la création d'objets {@link Product}.
 * Centralise toute la logique de construction, y compris la génération
 * du code produit au format SP-[TYPE]-[MATIERE]-YYYY-XX.
 *
 * Le numéro séquentiel est calculé dynamiquement depuis la base de données.
 */
@Component
@RequiredArgsConstructor
public class ProductFactory {

    private final ProductRepository productRepository;

    /**
     * Crée un objet Product complet, prêt à être persisté.
     *
     * @param type           entité ProductType (ex: name="FLO")
     * @param material       entité Material (ex: name="ROS")
     * @param quantity       quantité collectée
     * @param supplier       fournisseur associé
     * @param initialStatus  statut initial (ex: name="EN_COURS")
     * @param additionalInfo champs dynamiques personnalisables (nullable)
     * @return un Product avec productCode généré, prêt pour save()
     */
    public Product createProduct(ProductType type, Material material, Double quantity,
            Supplier supplier, Status initialStatus,
            java.time.LocalDateTime collectionDate,
            Map<String, Object> additionalInfo) {

        String code = generateProductCode(type, material);

        return Product.builder()
                .productCode(code)
                .type(type)
                .material(material)
                .quantity(quantity)
                .supplier(supplier)
                .collectionDate(collectionDate != null ? collectionDate : LocalDateTime.now())
                .currentStatus(initialStatus)
                .additionalInfo(additionalInfo != null ? additionalInfo : Map.of())
                .build();
    }

    /**
     * Génère le code produit au format SP-[TYPE]-[MATIERE]-YYYY-XX.
     * Le numéro séquentiel XX est calculé en comptant les produits existants en BDD
     * + 1.
     *
     * @param type     entité ProductType
     * @param material entité Material
     * @return le code produit formaté (ex: SP-FLO-ROS-2026-07)
     */
    public String generateProductCode(ProductType type, Material material) {
        int year = LocalDateTime.now().getYear();
        long nextSequence = productRepository.findMaxId() + 1;
        return String.format("SP-%s-%s-%d-%02d", type.getName(), material.getName(), year, nextSequence);
    }
}

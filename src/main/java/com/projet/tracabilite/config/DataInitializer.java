package com.projet.tracabilite.config;

import com.projet.tracabilite.entity.*;
import com.projet.tracabilite.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Amorçage de la base de données de production.
 * Remplace entièrement le fichier data.sql.
 * S'exécute au démarrage pour insérer les données de référence
 * et les utilisateurs par défaut si nécessaire.
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StepTypeRepository stepTypeRepository;
    private final StatusRepository statusRepository;
    private final ProductTypeRepository productTypeRepository;
    private final MaterialRepository materialRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public void run(String... args) {

        // ─── 1. Types d'étapes industrielles ───────────────────────────
        String[] mandatorySteps = {
                "RECEPTION_MATIERE",
                "TRANSFORMATION",
                "PURIFICATION_EXTRACTION",
                "CONDITIONNEMENT",
                "TERMINE"
        };

        for (String stepName : mandatorySteps) {
            if (stepTypeRepository.findByName(stepName).isEmpty()) {
                stepTypeRepository.save(StepType.builder().name(stepName).build());
            }
        }
        log.info(">> [DataInitializer] Types d'étapes vérifiés/insérés.");

        // ─── 2. Statuts correspondants ─────────────────────────────────
        String[] statuses = {
                "RECEPTION_MATIERE",
                "TRANSFORMATION",
                "PURIFICATION_EXTRACTION",
                "CONDITIONNEMENT",
                "TERMINE",
                "ANNULE"
        };

        for (String statusName : statuses) {
            if (statusRepository.findByName(statusName).isEmpty()) {
                statusRepository.save(Status.builder().name(statusName).build());
            }
        }
        log.info(">> [DataInitializer] Statuts vérifiés/insérés.");

        // ─── 3. Types de produit ───────────────────────────────────────
        String[] productTypes = { "FLO", "BIO", "CAR", "HUI" };

        for (String typeName : productTypes) {
            if (productTypeRepository.findByName(typeName).isEmpty()) {
                productTypeRepository.save(ProductType.builder().name(typeName).build());
            }
        }
        log.info(">> [DataInitializer] Types de produit vérifiés/insérés.");

        // ─── 4. Matières premières ─────────────────────────────────────
        String[] materials = { "ROS", "LAV", "ARG", "MEN" };

        for (String matName : materials) {
            if (materialRepository.findByName(matName).isEmpty()) {
                materialRepository.save(Material.builder().name(matName).build());
            }
        }
        log.info(">> [DataInitializer] Matières premières vérifiées/insérées.");

        // ─── 5. Fournisseurs ───────────────────────────────────────────
        initSupplier("FOU-OU-2026-01", "Fournisseur Arômes Casablanca");
        log.info(">> [DataInitializer] Fournisseurs vérifiés/insérés.");

        // ─── 6. Utilisateurs par défaut (admin + démo) ─────────────────

        // Vérification et création de l'admin
        if (userRepository.findByEmail("admin@tracabilite.ma").isEmpty()) {
            User admin = User.builder()
                    .email("admin@tracabilite.ma")
                    .password("admin123")
                    .build();
            userRepository.save(admin);
            log.info(">> [DataInitializer] Utilisateur admin créé avec succès.");
        } else {
            log.info(">> [DataInitializer] L'utilisateur admin existe déjà.");
        }

        // Vérification et création du compte démo
        if (userRepository.findByEmail("demo@tracabilite.ma").isEmpty()) {
            User demo = User.builder()
                    .email("demo@tracabilite.ma")
                    .password("demo123")
                    .build();
            userRepository.save(demo);
            log.info(">> [DataInitializer] Utilisateur démo créé avec succès.");
        } else {
            log.info(">> [DataInitializer] L'utilisateur démo existe déjà.");
        }

        log.info(">> [DataInitializer] Initialisation de la base de données terminée.");
    }

    /**
     * Insère un fournisseur s'il n'existe pas déjà (par code fournisseur).
     */
    private void initSupplier(String code, String name) {
        if (supplierRepository.findBySupplierCode(code).isEmpty()) {
            supplierRepository.save(Supplier.builder()
                    .supplierCode(code)
                    .name(name)
                    .build());
        }
    }
}

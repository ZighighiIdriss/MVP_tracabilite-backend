package com.projet.tracabilite.controller;

import com.projet.tracabilite.entity.Material;
import com.projet.tracabilite.entity.ProductType;
import com.projet.tracabilite.entity.StepType;
import com.projet.tracabilite.entity.Supplier;
import com.projet.tracabilite.repository.MaterialRepository;
import com.projet.tracabilite.repository.ProductTypeRepository;
import com.projet.tracabilite.repository.StepTypeRepository;
import com.projet.tracabilite.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur pour exposer les données de référence requises par le Frontend
 * (Remplissage des listes déroulantes / Dropdowns)
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReferenceController {

    private final ProductTypeRepository productTypeRepository;
    private final MaterialRepository materialRepository;
    private final SupplierRepository supplierRepository;
    private final StepTypeRepository stepTypeRepository;

    /**
     * Récupérer tous les types de produits (FLO, BIO, CAR, HUI).
     * GET /api/product-types
     */
    @GetMapping("/product-types")
    public ResponseEntity<List<ProductType>> getProductTypes() {
        return ResponseEntity.ok(productTypeRepository.findAll());
    }

    /**
     * Récupérer toutes les matières premières (ROS, LAV, etc.).
     * GET /api/materials
     */
    @GetMapping("/materials")
    public ResponseEntity<List<Material>> getMaterials() {
        return ResponseEntity.ok(materialRepository.findAll());
    }

    /**
     * Récupérer tous les fournisseurs enregistrés.
     * GET /api/suppliers
     */
    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getSuppliers() {
        return ResponseEntity.ok(supplierRepository.findAll());
    }

    /**
     * Récupérer tous les types d'étapes de traçabilité.
     * GET /api/step-types
     */
    @GetMapping("/step-types")
    public ResponseEntity<List<StepType>> getStepTypes() {
        return ResponseEntity.ok(stepTypeRepository.findAll());
    }
}
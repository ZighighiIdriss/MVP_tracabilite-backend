package com.projet.tracabilite.controller;

import com.projet.tracabilite.dto.AddStepRequest;
import com.projet.tracabilite.dto.CreateProductRequest;
import com.projet.tracabilite.dto.ProductDTO;
import com.projet.tracabilite.dto.StepDTO;
import com.projet.tracabilite.dto.UpdateProductRequest;
import com.projet.tracabilite.dto.UpdateStepRequest;
import com.projet.tracabilite.entity.Product;
import com.projet.tracabilite.entity.TraceabilityStep;
import com.projet.tracabilite.service.ProductService;
import com.projet.tracabilite.service.TraceabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final TraceabilityService traceabilityService;

    // ================================
    // Endpoints Produits (CRUD)
    // ================================

    /**
     * Créer un nouveau produit anonymisé.
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(
                request.getTypeId(),
                request.getMaterialId(),
                request.getQuantity(),
                request.getSupplierId(),
                request.getCollectionDate(),
                request.getInitialStepId(),
                request.getAdditionalInfo());

        ProductDTO dto = productService.getProductById(product.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Récupérer tous les produits.
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Récupérer le détail d'un produit.
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Mettre à jour un produit existant (mise à jour partielle).
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {

        ProductDTO dto = productService.updateProduct(id, request);
        return ResponseEntity.ok(dto);
    }

    /**
     * Supprimer un produit.
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ================================
    // Endpoints Étapes de traçabilité
    // ================================

    /**
     * Ajouter une étape de traçabilité à un produit.
     * POST /api/products/{id}/steps
     */
    @PostMapping("/{id}/steps")
    public ResponseEntity<StepDTO> addTraceabilityStep(
            @PathVariable Long id,
            @Valid @RequestBody AddStepRequest request) {

        TraceabilityStep step = traceabilityService.addStep(
                id,
                request.getStepName(),
                request.getDescription(),
                request.getLocation(),
                request.getDate(),
                request.getAdditionalInfo());

        StepDTO dto = traceabilityService.getStepById(step.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Récupérer les étapes de traçabilité d'un produit.
     * GET /api/products/{id}/steps
     */
    @GetMapping("/{id}/steps")
    public ResponseEntity<List<StepDTO>> getProductSteps(@PathVariable Long id) {
        return ResponseEntity.ok(traceabilityService.getStepsByProductId(id));
    }

    /**
     * Mettre à jour une étape de traçabilité (mise à jour partielle).
     * PUT /api/products/steps/{stepId}
     */
    @PutMapping("/steps/{stepId}")
    public ResponseEntity<StepDTO> updateStep(
            @PathVariable Long stepId,
            @Valid @RequestBody UpdateStepRequest request) {

        StepDTO dto = traceabilityService.updateStep(stepId, request);
        return ResponseEntity.ok(dto);
    }

    /**
     * Supprimer une étape de traçabilité.
     * DELETE /api/products/steps/{stepId}
     */
    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable Long stepId) {
        traceabilityService.deleteStep(stepId);
        return ResponseEntity.noContent().build();
    }
}

package com.projet.tracabilite.service;

import com.projet.tracabilite.dto.AddStepRequest;
import com.projet.tracabilite.dto.ProductDTO;
import com.projet.tracabilite.dto.StepDTO;
import com.projet.tracabilite.dto.UpdateProductRequest;
import com.projet.tracabilite.entity.Material;
import com.projet.tracabilite.entity.Product;
import com.projet.tracabilite.entity.ProductType;
import com.projet.tracabilite.entity.Status;
import com.projet.tracabilite.entity.Supplier;
import com.projet.tracabilite.entity.TraceabilityStep;
import com.projet.tracabilite.entity.StepType;
import com.projet.tracabilite.factory.ProductFactory;
import com.projet.tracabilite.repository.MaterialRepository;
import com.projet.tracabilite.repository.ProductRepository;
import com.projet.tracabilite.repository.ProductTypeRepository;
import com.projet.tracabilite.repository.StatusRepository;
import com.projet.tracabilite.repository.SupplierRepository;
import com.projet.tracabilite.repository.StepTypeRepository;
import com.projet.tracabilite.repository.TraceabilityStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductTypeRepository productTypeRepository;
    private final MaterialRepository materialRepository;
    private final StatusRepository statusRepository;
    private final ProductFactory productFactory;
    private final StepTypeRepository stepTypeRepository;
    private final TraceabilityStepRepository stepRepository;

    // ================================
    // CREATE
    // ================================

    @Override
    @Transactional
    public Product createProduct(Long typeId, Long materialId, Double quantity, Long supplierId,
            java.time.LocalDateTime collectionDate, Long initialStepId,
            Map<String, Object> additionalInfo) {

        // Récupération des entités de référence par ID
        ProductType type = productTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException(
                        "Type de produit introuvable avec l'ID : " + typeId));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException(
                        "Matière introuvable avec l'ID : " + materialId));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException(
                        "Fournisseur introuvable avec l'ID : " + supplierId));

        StepType initialStepType = stepTypeRepository.findById(initialStepId)
                .orElseThrow(() -> new RuntimeException("Type d'étape introuvable avec l'ID : " + initialStepId));

        Status initialStatus = statusRepository.findByName(initialStepType.getName())
                .orElseThrow(
                        () -> new RuntimeException("Statut introuvable en base pour : " + initialStepType.getName()));

        // Délégation à la Factory : elle génère le code et construit l'objet complet
        Product product = productFactory.createProduct(
                type, material, quantity, supplier, initialStatus, collectionDate, additionalInfo);

        Product savedProduct = productRepository.save(product);

        // ========== NOUVEAU : création automatique des 4 étapes industrielles ==========
        java.time.LocalDateTime baseDate = collectionDate != null ? collectionDate : java.time.LocalDateTime.now();
        String[] mandatoryStepNames = {
                "RECEPTION_MATIERE",
                "TRANSFORMATION",
                "PURIFICATION_EXTRACTION",
                "CONDITIONNEMENT",
                "TERMINE"
        };
        String[] mandatoryDescriptions = {
                "Réception et contrôle de la matière première",
                "Transformation de la matière première",
                "Purification et extraction",
                "Conditionnement et emballage final",
                "Produit terminé — lot clôturé"
        };

        for (int i = 0; i < mandatoryStepNames.length; i++) {
            // Résolution find-or-create du StepType par nom
            final String stepName = mandatoryStepNames[i];
            StepType stepType = stepTypeRepository.findByName(stepName)
                    .orElseGet(() -> {
                        StepType newType = StepType.builder().name(stepName).build();
                        return stepTypeRepository.save(newType);
                    });

            TraceabilityStep step = new TraceabilityStep();
            step.setProduct(savedProduct);
            step.setType(stepType);
            // Chaque étape reçoit une date décalée de +i minutes pour garantir l'ordre chrono
            step.setDate(baseDate.plusMinutes(i));
            step.setLocation("Usine Principale");
            step.setDescription(mandatoryDescriptions[i]);
            stepRepository.save(step);
        }

        return savedProduct;
    }

    // ================================
    // READ
    // ================================

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID : " + id));
        return mapToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ================================
    // UPDATE (mise à jour partielle)
    // ================================

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID : " + id));

        if (request.getTypeId() != null) {
            ProductType type = productTypeRepository.findById(request.getTypeId())
                    .orElseThrow(() -> new RuntimeException(
                            "Type de produit introuvable avec l'ID : " + request.getTypeId()));
            product.setType(type);
        }

        if (request.getMaterialId() != null) {
            Material material = materialRepository.findById(request.getMaterialId())
                    .orElseThrow(() -> new RuntimeException(
                            "Matière introuvable avec l'ID : " + request.getMaterialId()));
            product.setMaterial(material);
        }

        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }

        if (request.getStatusId() != null) {
            Status status = statusRepository.findById(request.getStatusId())
                    .orElseThrow(() -> new RuntimeException(
                            "Statut introuvable avec l'ID : " + request.getStatusId()));
            product.setCurrentStatus(status);
        }

        // ========== NOUVEAU : mise à jour du statut par nom (find-or-create) ==========
        if (request.getStatusName() != null && !request.getStatusName().isBlank()) {
            Status status = statusRepository.findByName(request.getStatusName())
                    .orElseGet(() -> {
                        Status newStatus = Status.builder()
                                .name(request.getStatusName())
                                .build();
                        return statusRepository.save(newStatus);
                    });
            product.setCurrentStatus(status);
        }

        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new RuntimeException(
                            "Fournisseur introuvable avec l'ID : " + request.getSupplierId()));
            product.setSupplier(supplier);
        }

        if (request.getAdditionalInfo() != null) {
            product.setAdditionalInfo(request.getAdditionalInfo());
        }

        productRepository.save(product);
        return mapToDTO(product);
    }

    // ================================
    // DELETE
    // ================================

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit introuvable avec l'ID : " + id);
        }
        productRepository.deleteById(id);
    }

    // ================================
    // ADD STEP TO PRODUCT
    // ================================

    @Override
    @Transactional
    public ProductDTO addStepToProduct(Long productId, AddStepRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID : " + productId));

        // ========== FIND-OR-CREATE : résolution dynamique du StepType par nom ==========
        StepType stepType = stepTypeRepository.findByName(request.getStepName())
                .orElseGet(() -> {
                    StepType newType = StepType.builder()
                            .name(request.getStepName())
                            .build();
                    return stepTypeRepository.save(newType);
                });

        // Créer l'étape
        TraceabilityStep step = new TraceabilityStep();
        step.setProduct(product);
        step.setType(stepType);
        step.setDate(request.getDate() != null ? request.getDate() : java.time.LocalDateTime.now());
        step.setLocation(request.getLocation());
        step.setDescription(request.getDescription());
        step.setAdditionalInfo(request.getAdditionalInfo());

        stepRepository.save(step);

        // Mettre à jour le statut du produit pour TOUTES les étapes (obligatoires ou sur-mesure)
        Status newStatus = statusRepository.findByName(stepType.getName())
                .orElseGet(() -> {
                    Status s = Status.builder().name(stepType.getName()).build();
                    return statusRepository.save(s);
                });

        product.setCurrentStatus(newStatus);
        productRepository.save(product);

        return mapToDTO(product);
    }

    // ================================
    // Mapping Entity → DTO
    // ================================

    private ProductDTO mapToDTO(Product product) {
        List<StepDTO> stepDTOs = Collections.emptyList();

        if (product.getSteps() != null) {
            stepDTOs = product.getSteps()
                    .stream()
                    .map(this::mapStepToDTO)
                    .collect(Collectors.toList());
        }

        String supplierCode = null;
        String supplierName = null;
        if (product.getSupplier() != null) {
            supplierCode = product.getSupplier().getSupplierCode();
            supplierName = product.getSupplier().getName();
        }

        return ProductDTO.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .type(product.getType() != null ? product.getType().getName() : null)
                .material(product.getMaterial() != null ? product.getMaterial().getName() : null)
                .quantity(product.getQuantity())
                .collectionDate(product.getCollectionDate())
                .currentStatus(product.getCurrentStatus() != null ? product.getCurrentStatus().getName() : null)
                .supplierCode(supplierCode)
                .supplierName(supplierName)
                .steps(stepDTOs)
                .additionalInfo(product.getAdditionalInfo())
                .build();
    }

    private StepDTO mapStepToDTO(TraceabilityStep step) {
        return StepDTO.builder()
                .id(step.getId())
                .type(step.getType() != null ? step.getType().getName() : null)
                .date(step.getDate())
                .location(step.getLocation())
                .description(step.getDescription())
                .additionalInfo(step.getAdditionalInfo())
                .build();
    }
}

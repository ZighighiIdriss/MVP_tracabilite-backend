package com.projet.tracabilite.service;

import com.projet.tracabilite.dto.StepDTO;
import com.projet.tracabilite.dto.UpdateStepRequest;
import com.projet.tracabilite.entity.Product;
import com.projet.tracabilite.entity.StepType;
import com.projet.tracabilite.entity.Status;
import com.projet.tracabilite.entity.TraceabilityStep;
import com.projet.tracabilite.repository.ProductRepository;
import com.projet.tracabilite.repository.StepTypeRepository;
import com.projet.tracabilite.repository.StatusRepository;
import com.projet.tracabilite.repository.TraceabilityStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraceabilityServiceImpl implements TraceabilityService {

    private final TraceabilityStepRepository traceabilityStepRepository;
    private final ProductRepository productRepository;
    private final StepTypeRepository stepTypeRepository;
    private final StatusRepository statusRepository;

    // ========== Noms des étapes obligatoires du processus industriel ==========
    private static final List<String> MANDATORY_STEP_NAMES = List.of(
            "RECEPTION_MATIERE",
            "TRANSFORMATION",
            "PURIFICATION_EXTRACTION",
            "CONDITIONNEMENT"
    );

    @Override
    @Transactional
    // ========== MODIFIÉ : Long typeId → String stepName ==========
    public TraceabilityStep addStep(Long productId, String stepName, String description, String location,
            java.time.LocalDateTime date,
            Map<String, Object> additionalInfo) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID : " + productId));

        // ========== FIND-OR-CREATE : résolution dynamique du StepType par nom ==========
        StepType stepType = stepTypeRepository.findByName(stepName)
                .orElseGet(() -> {
                    // Le type n'existe pas → création dynamique avec ID auto-généré
                    StepType newType = StepType.builder()
                            .name(stepName)
                            .build();
                    return stepTypeRepository.save(newType);
                });

        TraceabilityStep step = TraceabilityStep.builder()
                .type(stepType)
                .date(date != null ? date : LocalDateTime.now())
                .location(location != null ? location : "Lieu non précisé")
                .description(description)
                .product(product)
                .additionalInfo(additionalInfo != null ? additionalInfo : Map.of())
                .build();

        TraceabilityStep savedStep = traceabilityStepRepository.save(step);

        // Mise à jour du statut produit uniquement pour les étapes du processus obligatoire
        updateProductStatus(product, stepType);

        return savedStep;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StepDTO> getStepsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Produit introuvable avec l'ID : " + productId);
        }

        return traceabilityStepRepository.findByProductIdOrderByDateAsc(productId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StepDTO getStepById(Long id) {
        TraceabilityStep step = traceabilityStepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étape introuvable avec l'ID : " + id));

        return mapToDTO(step);
    }

    @Override
    @Transactional
    public StepDTO updateStep(Long stepId, UpdateStepRequest request) {
        TraceabilityStep step = traceabilityStepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Étape introuvable avec l'ID : " + stepId));

        // Mise à jour conditionnelle du type d'étape
        if (request.getTypeId() != null) {
            StepType stepType = stepTypeRepository.findById(request.getTypeId())
                    .orElseThrow(() -> new RuntimeException(
                            "Type d'étape introuvable avec l'ID : " + request.getTypeId()));
            step.setType(stepType);
        }

        // Mise à jour conditionnelle de la description
        if (request.getDescription() != null) {
            step.setDescription(request.getDescription());
        }

        // Mise à jour conditionnelle de la localisation
        if (request.getLocation() != null) {
            step.setLocation(request.getLocation());
        }

        // Mise à jour conditionnelle des champs dynamiques JSON
        if (request.getAdditionalInfo() != null) {
            step.setAdditionalInfo(request.getAdditionalInfo());
        }

        traceabilityStepRepository.save(step);
        return mapToDTO(step);
    }

    @Override
    @Transactional
    public void deleteStep(Long stepId) {
        if (!traceabilityStepRepository.existsById(stepId)) {
            throw new RuntimeException("Étape introuvable avec l'ID : " + stepId);
        }
        traceabilityStepRepository.deleteById(stepId);
    }

    // ================================
    // Logique métier
    // ================================

    /**
     * Met à jour le statut du produit en fonction du type d'étape ajoutée.
     * Seules les étapes du processus obligatoire (4 étapes industrielles)
     * déclenchent une progression du statut. Les étapes personnalisées
     * (sur-mesure) n'affectent pas le statut du produit.
     */
    private void updateProductStatus(Product product, StepType stepType) {
        String stepTypeName = stepType.getName();

        // Les étapes personnalisées (hors processus obligatoire) ne changent pas le statut
        if (!MANDATORY_STEP_NAMES.contains(stepTypeName)) {
            return;
        }

        // Progression du statut calquée sur la timeline industrielle
        String statusName = switch (stepTypeName) {
            case "RECEPTION_MATIERE" -> "RECEPTION_MATIERE";
            case "TRANSFORMATION" -> "TRANSFORMATION";
            case "PURIFICATION_EXTRACTION" -> "PURIFICATION_EXTRACTION";
            case "CONDITIONNEMENT" -> "TERMINE";
            default -> null;
        };

        if (statusName != null) {
            Status status = statusRepository.findByName(statusName)
                    .orElseThrow(() -> new RuntimeException(
                            "Statut '" + statusName + "' introuvable en base de données"));
            product.setCurrentStatus(status);
            productRepository.save(product);
        }
    }

    // ================================
    // Mapping Entity → DTO
    // ================================

    private StepDTO mapToDTO(TraceabilityStep step) {

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

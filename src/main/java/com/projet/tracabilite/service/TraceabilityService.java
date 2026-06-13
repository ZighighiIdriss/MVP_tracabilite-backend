package com.projet.tracabilite.service;

import com.projet.tracabilite.dto.StepDTO;
import com.projet.tracabilite.dto.UpdateStepRequest;
import com.projet.tracabilite.entity.TraceabilityStep;

import java.util.List;
import java.util.Map;

public interface TraceabilityService {

    // ========== Long typeId → String stepName ==========
    TraceabilityStep addStep(Long productId, String stepName, String description, String location,
            java.time.LocalDateTime date, Map<String, Object> additionalInfo);

    List<StepDTO> getStepsByProductId(Long productId);

    StepDTO getStepById(Long id);

    StepDTO updateStep(Long stepId, UpdateStepRequest request);

    void deleteStep(Long stepId);

}

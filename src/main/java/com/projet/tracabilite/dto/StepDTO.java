package com.projet.tracabilite.dto;

import lombok.*;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * DTO pour l'anonymisation et le transfert des données d'étape de traçabilité.
 *
 * pour que le frontend puisse afficher directement le libellé.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepDTO {

    private Long id;

    // ========== StepType type → String type (nom de l'entité) ==========
    private String type;

    private LocalDateTime date;
    private String location;
    private String description;
    private Map<String, Object> additionalInfo;
}

package com.projet.tracabilite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "traceability_steps")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraceabilityStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========== enum StepType → @ManyToOne vers l'entité StepType
    // ==========
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_type_id", nullable = false)
    private StepType type;

    private LocalDateTime date;

    @Builder.Default
    @Column(nullable = false)
    private String location = "Casablanca";

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // ========== Champs dynamiques JSON (paires clé/valeur personnalisables)
    // ==========
    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> additionalInfo = new HashMap<>();
}

package com.projet.tracabilite.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Les valeurs sont désormais gérées dynamiquement en base de données.
 */
@Entity
@Table(name = "materials")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom/code de la matière (ex: "ROS", "LAV", "ARG", "MEN") */
    @Column(unique = true, nullable = false)
    private String name;
}

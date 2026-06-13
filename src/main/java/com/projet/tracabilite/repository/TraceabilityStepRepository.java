package com.projet.tracabilite.repository;

import com.projet.tracabilite.entity.TraceabilityStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraceabilityStepRepository extends JpaRepository<TraceabilityStep, Long> {

    List<TraceabilityStep> findByProductIdOrderByDateAsc(Long productId);
}

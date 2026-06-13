package com.projet.tracabilite.repository;

import com.projet.tracabilite.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    /** Compte les produits dont le statut correspond au nom donné. */
    long countByCurrentStatusName(String name);

    /** Compte les produits dont le statut N'EST PAS dans la liste fournie (générique). */
    long countByCurrentStatusNameNotIn(List<String> statusNames);

    List<Product> findTop5ByOrderByIdDesc();

    @Query("SELECT COALESCE(MAX(p.id), 0) FROM Product p")
    Long findMaxId();
}

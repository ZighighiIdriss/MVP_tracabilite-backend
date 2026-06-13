package com.projet.tracabilite.service;

import com.projet.tracabilite.dto.AddStepRequest;
import com.projet.tracabilite.dto.ProductDTO;
import com.projet.tracabilite.dto.UpdateProductRequest;
import com.projet.tracabilite.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product createProduct(Long typeId, Long materialId, Double quantity, Long supplierId,
            java.time.LocalDateTime collectionDate, Long initialStepId,
            Map<String, Object> additionalInfo);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts();

    ProductDTO updateProduct(Long id, UpdateProductRequest request);

    void deleteProduct(Long id);

    ProductDTO addStepToProduct(Long productId, AddStepRequest request);
}

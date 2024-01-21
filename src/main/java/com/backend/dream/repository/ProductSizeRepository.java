package com.backend.dream.repository;

import com.backend.dream.entity.Product;
import com.backend.dream.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize,Long> {
    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.id = :productId")
    List<ProductSize> findAllByProductId(Long productId);

    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.id = :productId AND ps.size.id = :sizeId")
    Optional<ProductSize> findByProductIdAndSizeId(@Param("productId") Long productId, @Param("sizeId") Long sizeId);

    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.id = :productId AND ps.size.id = :sizeId")
    ProductSize findProductSizeById(@Param("productId") Long productId, @Param("sizeId") Long sizeId);


}

package com.backend.dream.repository;

import com.backend.dream.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE p.name LIKE ?1 AND p.active = true " +
            "AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id)")
    List<Product> findByName(String name);

    @Query("SELECT p FROM Product p where category.id=?1 AND p.active = true " +
            "AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id)")
    Page<Product> findByCategoryID(Long categoryId, Pageable pageable);


    // For Sort products function
    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.active = true " +
            "AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id) ORDER BY p.price ASC")
    Page<Product> findByCategoryOrderByPriceAsc(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.active = true " +
            "AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id) ORDER BY p.price DESC")
    Page<Product> findByCategoryOrderByPriceDesc(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE lower(concat('%', :productName, '%')) " +
            "AND p.active = true " +
            "AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id)")
    Page<Product> findByNameContainingIgnoreCase(String productName, Pageable pageable);

    // Discount products
    @Query("SELECT p FROM Product p JOIN p.category.discount d WHERE d.activeDate <= current_date AND " +
            "d.expiredDate >= current_date AND p.active = true AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id)")
    Page<Product> findSaleProducts(Pageable pageable);

    // Change price of product according to the chosen size
    @Query("SELECT ps.price FROM ProductSize ps WHERE ps.product.id = :productId AND ps.size.id = :sizeId")
    Double findProductPriceBySize(@Param("productId") Long productId, @Param("sizeId") Long sizeId);

    // Sort by average rating
    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN p.feedback f " +
            "WHERE p.category.id = ?1 AND p.active = true AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id) " +
            "GROUP BY p.id " +
            "ORDER BY AVG(f.rating) DESC")
    Page<Product> findByTopRating(@Param("categoryId") Long categoryId, Pageable pageable);

//     Sort by best-seller
@Query(value = "SELECT p "
        + "FROM OrderDetails od "
        + "JOIN od.product p "
        + "JOIN od.orders o "
        + "WHERE o.status.id = ?1 and p.category.id = ?1 AND p.active = true AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.product.id = p.id) "
        + "GROUP BY p.id, p.name "
        + "ORDER BY SUM(od.quantity) DESC")
    Page<Product> findByBestseller(@Param("categoryId") Long categoryId, Pageable pageable);

    // For admin interface
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> searchByName(@Param("name") String name);
}
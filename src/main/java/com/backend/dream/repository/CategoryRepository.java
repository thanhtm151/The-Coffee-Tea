package com.backend.dream.repository;

import com.backend.dream.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c "
            + "FROM Category c "
            + "JOIN c.discount d "
            + "WHERE c.id = :idCategory and d.active = true and current_date >= d.activeDate and current_date <= d.expiredDate")
    Optional<Category> findByIDCategory(Long idCategory);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Category a WHERE a.name = :name")
    boolean findByname(@Param("name") String name);
}

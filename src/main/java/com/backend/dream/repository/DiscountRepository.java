package com.backend.dream.repository;

import com.backend.dream.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {

    @Query("SELECT d FROM Discount d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Discount> searchByName(@Param("name") String name);
    List<Discount> findByActiveDateBeforeAndExpiredDateAfter(Date currentDate, Date currentDate1);
}

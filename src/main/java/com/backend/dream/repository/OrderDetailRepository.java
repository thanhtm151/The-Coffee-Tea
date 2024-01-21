package com.backend.dream.repository;

import com.backend.dream.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails,Long> {
    @Query("SELECT od FROM OrderDetails od WHERE od.orders.id =?1")
    List<OrderDetails> findByID(Long id);
}

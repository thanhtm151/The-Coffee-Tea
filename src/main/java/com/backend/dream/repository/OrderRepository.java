package com.backend.dream.repository;

import com.backend.dream.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    @Query("SELECT o FROM Orders o WHERE o.account.username = ?1  ORDER BY o.createDate DESC, o.createTime DESC")
    Page<Orders> listOrdersByUsername(String username, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.status.id = ?1")
    List<Orders> getListOrder(int id);

    @Query("SELECT o FROM Orders o WHERE o.status.id = :statusID AND LOWER(o.account.fullname) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<Orders> findByAccountUsername(Long statusID, String username);

}

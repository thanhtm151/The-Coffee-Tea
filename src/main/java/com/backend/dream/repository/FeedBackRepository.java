package com.backend.dream.repository;

import com.backend.dream.dto.AccountDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.FeedBack;
import com.backend.dream.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack,Long> {
    @Query("SELECT f FROM FeedBack f WHERE f.product.id = :productId")
    List<FeedBack> findFeedbacksByProductId(@Param("productId") Long productId);

    @Query("SELECT fb.account FROM FeedBack fb WHERE fb.id = :feedbackId")
    Account findAccountByFeedBackId(@Param("feedbackId") Long feedbackId);

    @Query("SELECT f FROM FeedBack f WHERE f.product.id = :productId AND f.rating = :rating")
    List<FeedBack> findByProductIdAndRating(@Param("productId") Long productId, @Param("rating") int rating);

    @Query("SELECT COUNT(*) FROM FeedBack f WHERE f.product.id = :productId")
    Long countByProductId(Long productId);


}

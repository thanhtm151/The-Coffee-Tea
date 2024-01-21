package com.backend.dream.service;

import com.backend.dream.dto.AccountDTO;
import com.backend.dream.dto.FeedBackDTO;
import com.backend.dream.entity.Account;

import java.util.List;

public interface FeedbackService {
    List<FeedBackDTO> getFeedbacksForProduct(Long productId);
//    Account findAccountByFeedBackId(Long feedbackId);

    void createFeedback(Long productId, Long accountId, String comment, int rating, String imageFileName);

    // Calculate average rating feedbacks
    double getAverageRating(Long productId);

    List<FeedBackDTO> getFeedbacksForProductByRating(Long productId, int rating);

    void deleteFeedback(Long id);

    Long countFeedback(Long productId);




}

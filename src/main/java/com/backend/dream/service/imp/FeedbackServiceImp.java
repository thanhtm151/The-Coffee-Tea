package com.backend.dream.service.imp;

import com.backend.dream.dto.FeedBackDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.FeedBack;
import com.backend.dream.mapper.FeedBackMapper;
import com.backend.dream.repository.FeedBackRepository;
import com.backend.dream.repository.ProductRepository;
import com.backend.dream.service.AccountService;
import com.backend.dream.service.FeedbackService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Transactional
@Service
public class FeedbackServiceImp implements FeedbackService {

    private final FeedBackRepository feedBackRepository;

    private final FeedBackMapper feedBackMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountService accountService;


    public FeedbackServiceImp(FeedBackRepository feedBackRepository, FeedBackMapper feedBackMapper) {
        this.feedBackRepository = feedBackRepository;
        this.feedBackMapper = feedBackMapper;
    }

    @Override
    public List<FeedBackDTO> getFeedbacksForProduct(Long productId) {
        List<FeedBack> feedbackList = feedBackRepository.findFeedbacksByProductId(productId);
        return feedbackList.stream()
                .map(feedBackMapper::feedBackToFeedBackDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public Account findAccountByFeedBackId(Long feedbackId) {
//        return feedBackRepository.findAccountByFeedBackId(feedbackId);
//    }

    @Override
    public void createFeedback(Long productId, Long accountId, String comment, int rating, String imageFileName) {
        FeedBack feedback = new FeedBack();
        feedback.setProduct(productRepository.findById(productId).orElse(null));
        feedback.setAccount(accountService.findById(String.valueOf(accountId)));

        feedback.setNote(comment);
        feedback.setRating(rating);
        feedback.setImage(imageFileName);
        // Lấy thời gian hiện tại
        Date currentDate = new Date(System.currentTimeMillis());
        Time currentTime = new Time(System.currentTimeMillis());
        feedback.setCreateDate(currentDate);
        feedback.setCreateTime(currentTime);

        feedBackRepository.save(feedback);
    }

    @Override
    public double getAverageRating(Long productId) {
        List<FeedBack> feedbackList = feedBackRepository.findFeedbacksByProductId(productId);

        if (feedbackList.isEmpty()) {
            return 0.0;
        }

        double sum = feedbackList.stream().mapToDouble(FeedBack::getRating).sum();
        double averageRating = sum / feedbackList.size();
        return Math.round(averageRating);
    }

    @Override
    public List<FeedBackDTO> getFeedbacksForProductByRating(Long productId, int rating) {
        // Gọi phương thức từ repository hoặc mapper để lấy danh sách bình luận với số sao đã chọn
        List<FeedBack> feedbackList = feedBackRepository.findByProductIdAndRating(productId, rating);

        // Chuyển danh sách bình luận sang DTO nếu cần
        List<FeedBackDTO> feedbackDTOList = feedbackList.stream()
                .map(feedback -> feedBackMapper.feedBackToFeedBackDTO(feedback))
                .collect(Collectors.toList());

        return feedbackDTOList;
    }

    @Override
    public void deleteFeedback(Long id) {
        Optional<FeedBack> feedbackOptional = feedBackRepository.findById(id);

        if (feedbackOptional.isPresent()) {
            FeedBack feedback = feedbackOptional.get();
            feedBackRepository.delete(feedback);
        } else {
            throw new EntityNotFoundException("Feedback not found: " + id);
        }
    }


    @Override
    public Long countFeedback(Long productId) {
        return feedBackRepository.countByProductId(productId);
    }
}

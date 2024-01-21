package com.backend.dream.repository;

import com.backend.dream.entity.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Token findByToken(String token);

    @Transactional
    public void deleteByExpiredDateBefore(LocalDateTime now);
}

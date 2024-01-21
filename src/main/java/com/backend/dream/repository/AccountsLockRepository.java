package com.backend.dream.repository;

import com.backend.dream.entity.AccountsLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountsLockRepository extends JpaRepository<AccountsLock, Long> {
    List<AccountsLock> findByAccountId(Long accountId);
    int countByAccountId(Long accountId);

}

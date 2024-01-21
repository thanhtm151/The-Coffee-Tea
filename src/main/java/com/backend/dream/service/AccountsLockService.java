package com.backend.dream.service;

import com.backend.dream.dto.AccountsLockDTO;
import com.backend.dream.entity.AccountsLock;

import java.util.List;

public interface AccountsLockService {
    AccountsLockDTO save(AccountsLockDTO accountsLockDTO);

    int getLockCountByAccountId(Long accountId);

    List<AccountsLock> getLockDetailsByAccountId(Long accountId);
}

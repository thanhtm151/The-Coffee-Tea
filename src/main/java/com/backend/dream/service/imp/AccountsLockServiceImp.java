package com.backend.dream.service.imp;

import com.backend.dream.dto.AccountsLockDTO;
import com.backend.dream.entity.AccountsLock;
import com.backend.dream.mapper.AccountsLockMapper;
import com.backend.dream.repository.AccountsLockRepository;
import com.backend.dream.service.AccountsLockService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public class AccountsLockServiceImp implements AccountsLockService {
    @Autowired
    private AccountsLockRepository accountsLockRepository;

    @Autowired
    private AccountsLockMapper accountsLockMapper;

    @Override
    public AccountsLockDTO save(AccountsLockDTO accountsLockDTO) {
        AccountsLock accountsLock = accountsLockMapper.AccountsLockDTOtoAccountsLock(accountsLockDTO);
        return accountsLockMapper.AccountsLocktoAccountsLocksDTO(accountsLockRepository.save(accountsLock));
    }

    @Override
    public int getLockCountByAccountId(Long accountId) {
        return accountsLockRepository.countByAccountId(accountId);
    }

    @Override
    public List<AccountsLock> getLockDetailsByAccountId(Long accountId) {
        return accountsLockRepository.findByAccountId(accountId);
    }


}

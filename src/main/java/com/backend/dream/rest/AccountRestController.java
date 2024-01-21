package com.backend.dream.rest;

import com.backend.dream.dto.AccountDTO;
import com.backend.dream.dto.AccountDTO;
import com.backend.dream.dto.AccountsLockDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.AccountsLock;
import com.backend.dream.mapper.AccountMapper;
import com.backend.dream.service.AccountService;
import com.backend.dream.service.AccountsLockService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/profile")
public class AccountRestController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountsLockService accountsLockService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getOne(@PathVariable("id") Long id) {
        try {
            AccountDTO accountDTO = accountService.findById(id);
            if (accountDTO != null) {
                return new ResponseEntity<>(accountDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> update(@RequestBody AccountDTO accountDTO, @PathVariable("id") Long id) {
        try {
            AccountDTO updatedAccountDTO = accountService.updateAccount(accountDTO);
            if (updatedAccountDTO != null) {
                return new ResponseEntity<>(updatedAccountDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/authenticate/{id}")
    public boolean authenticate(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        String password = body.get("password");
        AccountDTO accountDTO = accountService.findById(id);
        return passwordEncoder.matches(password, accountDTO.getPassword());
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<AccountDTO> updatePassword(@PathVariable("id") Long id,
                                                     @RequestBody Map<String, String> requestBody) {
        String newPassword = requestBody.get("password");
        AccountDTO accountDTO = accountService.findById(id);
        accountService.updatePassword(accountDTO, newPassword);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<AccountDTO> lockAccount(@RequestBody AccountsLockDTO lockData) {
        try {
            AccountDTO accountDTO = accountService.findById(lockData.getAccountId());
            if (accountDTO != null && accountDTO.isActive()) {
                accountDTO.setActive(false);
                accountService.updateAccount(accountDTO);

                AccountsLockDTO accountsLockDTO = new AccountsLockDTO();
                accountsLockDTO.setAccountId(lockData.getAccountId());
                accountsLockDTO.setReason(lockData.getReason());
                accountsLockDTO.setBanDate(new Timestamp(System.currentTimeMillis()));
                accountsLockService.save(accountsLockDTO);
                return new ResponseEntity<>(accountDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/unlock/{id}")
    public ResponseEntity<AccountDTO> unlockAccount(@PathVariable("id") Long id) {
        try {
            AccountDTO accountDTO = accountService.findById(id);
            accountDTO.setActive(true);
            AccountDTO updatedAccountDTO = accountService.updateAccount(accountDTO);
            if (updatedAccountDTO != null) {
                return new ResponseEntity<>(updatedAccountDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/lockDetails/{id}")
    public ResponseEntity<Map<String, Object>> getLockDetailsByAccountId(@PathVariable("id") Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            int lockCount = accountsLockService.getLockCountByAccountId(id);
            List<AccountsLock> lockDetails = accountsLockService.getLockDetailsByAccountId(id);

            response.put("lockCount", lockCount);
            response.put("lockDetails", lockDetails);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}

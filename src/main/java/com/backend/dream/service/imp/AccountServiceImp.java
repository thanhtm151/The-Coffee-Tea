package com.backend.dream.service.imp;

import com.backend.dream.dto.AccountDTO;
import com.backend.dream.dto.AuthorityDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.Authority;
import com.backend.dream.entity.Role;
import com.backend.dream.mapper.AccountMapper;
import com.backend.dream.repository.AccountRepository;
import com.backend.dream.repository.AuthorityRepository;
import com.backend.dream.repository.RoleRepository;
import com.backend.dream.service.AccountService;
import com.backend.dream.util.ExcelUltils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class AccountServiceImp implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public Long findRoleIdByUsername(String username) {
        return accountRepository.findRoleIdByUsername(username);
    }

    @Override
    public AccountDTO registerAccount(AccountDTO accountDTO) {
        Account account = accountMapper.accountDTOToAccount(accountDTO);
        account.setFullname(accountDTO.getFirstname() + " " + accountDTO.getLastname());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setAvatar("testimonial-0.jpg");
        Account saveAccount = accountRepository.save(account);
        return accountMapper.accountToAccountDTO(saveAccount);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account account = accountMapper.accountDTOToAccount(accountDTO);
        account.setFullname(accountDTO.getFirstname() + " " + accountDTO.getLastname());
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.accountToAccountDTO(updatedAccount);
    }

    public Long findIDByUsername(String username) throws NoSuchElementException {
        return accountRepository.findIdByUsername(username);
    }

    @Override
    public AccountDTO findById(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        return accountOptional.isPresent() ? accountMapper.accountToAccountDTO(accountOptional.get()) : null;
    }

    @Override
    public String findFullNameByUsername(String username) throws NoSuchElementException {
        return accountRepository.findFullNameByUsername(username);
    }

    @Override
    public String getImageByUserName(String remoteUser) throws NoSuchElementException {
        return accountRepository.getImageByUsername(remoteUser);
    }

    @Override
    public Account findByUsernameAndEmail(String username, String email) {
        return accountRepository.findByUsernameAndEmail(username, email);
    }

    @Override
    public AccountDTO updatePassword(AccountDTO accountDTO, String password) {
        Account account = accountMapper.accountDTOToAccount(accountDTO);
        account.setPassword(passwordEncoder.encode(password));
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.accountToAccountDTO(updatedAccount);
    }

    @Override
    public List<Account> getStaff() {
        return accountRepository.getStaff();
    }

    @Override
    public List<Account> findALL() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateStaff(Account staffToUpdate) {
        return accountRepository.save(staffToUpdate);
    }

    @Override
    public String getAddressByUsername(String remoteUser) {
        return accountRepository.getAddressByUsername(remoteUser);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> list = accountMapper.listAccountToListAccountDTO(accountRepository.findAll());
        return list;
    }

    @Override
    public ByteArrayInputStream getdataStaff() throws IOException {
        List<Account> accounts = accountRepository.getStaff();
        ByteArrayInputStream data = ExcelUltils.dataToExcel(accounts, ExcelUltils.SHEET_NAMESTAFF,ExcelUltils.HEADERSTAFF);
        return data;
    }

    // @Override
    // public Account createAccountWhenDontHaveAccount(Account account) {
    // account.setPassword(passwordEncoder.encode(account.getPassword()));
    // return accountRepository.save(account);
    // }

    @Override
    public Account createStaff(AccountDTO accountDTO) {
        String password = passwordEncoder.encode(accountDTO.getPassword());
        Account newAccount = accountMapper.accountDTOToAccount(accountDTO);

        newAccount.setPassword(password);

        Role role = new Role();
        role.setId(Long.valueOf(2L));

        Authority authority = new Authority();
        authority.setRole(role);

        newAccount.setFullname(newAccount.getFirstname() + " " + newAccount.getLastname());
        Account savedAccount = accountRepository.save(newAccount);

        authority.setAccount(savedAccount);
        Authority savedAuthority = authorityRepository.save(authority);

        return savedAccount;
    }

    @Override
    public Account findById(String username) {
        return accountRepository.findById(Long.valueOf(username)).get();
    }

    @Override
    public List<AccountDTO> searchAccount(String name) {
        List<Account> accounts = accountRepository.searchAccount(name);
        return accounts.stream()
                .map(accountMapper::accountToAccountDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getUsersByRole(Long roleID) {
        List<Account> usersByRole = accountRepository.getUsersByRole(roleID);
        return usersByRole.stream()
                .map(accountMapper::accountToAccountDTO)
                .collect(Collectors.toList());
    }

}

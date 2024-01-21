package com.backend.dream.service.imp;


import com.backend.dream.dto.AuthorityDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.Authority;
import com.backend.dream.mapper.AuthorityMapper;
import com.backend.dream.repository.AccountRepository;
import com.backend.dream.repository.AuthorityRepository;
import com.backend.dream.service.AuthorityService;
import com.backend.dream.util.ExcelUltils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class AuthorityServiceImp implements AuthorityService {

	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	private AuthorityMapper authorityMapper;
	
	@Override
	public List<Authority> getAdmin() {
		List<Account> accounts = accountRepository.getStaff();
		return authorityRepository.authoritiesOf(accounts);
	}

	@Override
	public List<Authority> findALL() {
		return authorityRepository.findAll();
	}

	@Override
	public void delete(Long id) {
		authorityRepository.deleteById(id);
	}

	@Override
	public ByteArrayInputStream getdataAuthority() throws IOException {
		List<Authority> authorities = authorityRepository.findAll();
		ByteArrayInputStream data = ExcelUltils.dataToExcel(authorities, ExcelUltils.SHEET_NAMEAUTHORITY,ExcelUltils.HEADERAUTHORITY);
		return data;
	}

	@Override
	public Authority create(Authority authority) {
		return authorityRepository.save(authority);
	}


}

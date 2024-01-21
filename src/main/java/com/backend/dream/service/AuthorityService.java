package com.backend.dream.service;


import com.backend.dream.dto.AuthorityDTO;
import com.backend.dream.entity.Authority;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface AuthorityService {

	List<Authority> getAdmin();

	List<Authority> findALL();

	Authority create(Authority authority);

	void delete(Long id);

	ByteArrayInputStream getdataAuthority() throws IOException;
}

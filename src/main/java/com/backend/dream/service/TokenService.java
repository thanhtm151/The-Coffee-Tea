package com.backend.dream.service;

import com.backend.dream.dto.TokenDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.Token;

public interface TokenService {
	public TokenDTO createTokenForUser(Account account);

	public Token findByToken(String token);

}

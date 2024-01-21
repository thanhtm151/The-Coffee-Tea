package com.backend.dream.service.imp;

import java.time.LocalDateTime;
import java.util.Random;

import com.backend.dream.dto.TokenDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.Token;
import com.backend.dream.mapper.AccountMapper;
import com.backend.dream.mapper.TokenMapper;
import com.backend.dream.repository.TokenRepository;
import com.backend.dream.service.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
@Transactional
@Service
@EnableScheduling
public class TokenServiceImp implements TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private TokenMapper tokenMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public TokenDTO createTokenForUser(Account account) {
		Random random = new Random();
		int tokenInt = random.nextInt(1000000);
		String createtoken = String.format("%06d", tokenInt);

		Token token = new Token();
		token.setToken(createtoken);
		token.setTokenType("Password");
		token.setAccount(account);

		LocalDateTime expiry_date = LocalDateTime.now().plusMinutes(5);
		token.setExpiredDate(expiry_date);
		tokenRepository.save(token);
		return tokenMapper.tokenToTokenDTO(token);
	}

	@Override
	public com.backend.dream.entity.Token findByToken(String token) {
		return tokenRepository.findByToken(token);
	}

}

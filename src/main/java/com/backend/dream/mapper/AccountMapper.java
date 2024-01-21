package com.backend.dream.mapper;


import com.backend.dream.dto.AccountDTO;
import com.backend.dream.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO accountToAccountDTO(Account account);
    List<AccountDTO> listAccountToListAccountDTO(List<Account> account);
    Account accountDTOToAccount(AccountDTO accountDTO);
}

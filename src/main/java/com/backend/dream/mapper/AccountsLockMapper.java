package com.backend.dream.mapper;

import com.backend.dream.dto.AccountsLockDTO;
import com.backend.dream.entity.AccountsLock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountsLockMapper {
    AccountsLockMapper INSTANCE = Mappers.getMapper(AccountsLockMapper.class);

    @Mapping(target = "accountId", source = "account.id")
    AccountsLockDTO AccountsLocktoAccountsLocksDTO(AccountsLock accountsLock);

    List<AccountsLockDTO> AccountsLockListtoAccountsLockListDTO(List<AccountsLock> accountsLockList);

    @Mapping(target = "account.id", source = "accountId")
    AccountsLock AccountsLockDTOtoAccountsLock(AccountsLockDTO accountsLockDTO);

    List<AccountsLock> AccountsLockListDTOtoAccountsLockList(List<AccountsLockDTO> accountsLockDTOList);

}

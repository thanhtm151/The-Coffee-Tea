package com.backend.dream.mapper;

import com.backend.dream.dto.TokenDTO;
import com.backend.dream.entity.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

    @Mapping(source = "account.id", target = "id_account")
    TokenDTO tokenToTokenDTO(Token token);

    @Mapping(source = "id_account", target = "account.id")
    Token tokenDTOToToken(TokenDTO tokenDTO);
}


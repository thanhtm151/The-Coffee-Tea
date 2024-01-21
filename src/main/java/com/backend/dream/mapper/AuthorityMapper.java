package com.backend.dream.mapper;

import com.backend.dream.dto.AuthorityDTO;
import com.backend.dream.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    @Mapping(source = "account.id", target= "id_account")
    @Mapping(source = "role.id", target = "id_role")
    AuthorityDTO authorityToAuthorityDTO(Authority authority);
    @Mapping(source = "id_account", target="account.id")
    @Mapping(source = "id_role", target="role.id")
    Authority authorityDTOToAuthority(AuthorityDTO authorityDTO);

}

package com.backend.dream.mapper;

import com.backend.dream.dto.RoleDTO;
import com.backend.dream.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    public RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
   public RoleDTO roleToRoleDTO(Role role);
   public Role roleDTOToRole(RoleDTO roleDTO);
}

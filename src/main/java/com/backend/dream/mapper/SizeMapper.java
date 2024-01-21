package com.backend.dream.mapper;

import com.backend.dream.dto.SizeDTO;
import com.backend.dream.entity.Size;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    SizeMapper INSTANCE = Mappers.getMapper(SizeMapper.class);

    SizeDTO sizeToSizeDTO(Size size);
    Size sizeDTOToSize(SizeDTO sizeDTO);
    List<SizeDTO> listSizeToListSizeDTO(List<Size> size);
}

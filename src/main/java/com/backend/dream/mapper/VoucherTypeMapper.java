package com.backend.dream.mapper;

import com.backend.dream.dto.VoucherTypeDTO;
import com.backend.dream.entity.VoucherType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VoucherTypeMapper {
    VoucherTypeMapper INSTANCE = Mappers.getMapper(VoucherTypeMapper.class);
    VoucherTypeDTO voucherTypeToVoucherTypeDTO(VoucherType voucherType);
    VoucherType voucherTypeDTOToVoucherType(VoucherTypeDTO voucherDTO);

}

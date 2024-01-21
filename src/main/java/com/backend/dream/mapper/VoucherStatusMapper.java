package com.backend.dream.mapper;

import com.backend.dream.dto.VoucherStatusDTO;
import com.backend.dream.entity.VoucherStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VoucherStatusMapper {
    VoucherStatusMapper INSTANCE = Mappers.getMapper(VoucherStatusMapper.class);
    VoucherStatusDTO voucherStatusToVoucherStatusDTO(VoucherStatus voucherStatus);
    VoucherStatus voucherStatusDTOToVoucherStatus(VoucherStatusDTO voucherStatusDTO);
}

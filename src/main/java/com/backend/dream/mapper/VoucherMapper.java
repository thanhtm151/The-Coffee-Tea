package com.backend.dream.mapper;

import com.backend.dream.dto.VoucherDTO;
import com.backend.dream.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);

    @Mapping(source = "account.id",target = "id_account")
    @Mapping(source = "status.id", target = "status")
    @Mapping(source = "type.id", target = "type")
    VoucherDTO voucherToVoucherDTO(Voucher voucher);

    @Mapping(source = "id_account",target = "account.id")
    @Mapping(source = "status",target = "status.id")
    @Mapping(source = "type",target = "type.id")
    Voucher voucherDTOToVoucher(VoucherDTO voucherDTO);

    @Mapping(source = "id_account",target = "account.id")
    @Mapping(source = "status",target = "status.id")
    @Mapping(source = "type",target = "type.id")
    List<Voucher> listVoucherDTOToListVoucher(List<VoucherDTO> voucherDTO);


}

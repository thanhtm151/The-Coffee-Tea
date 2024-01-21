package com.backend.dream.service;

import com.backend.dream.dto.VoucherDTO;
import com.backend.dream.dto.VoucherStatusDTO;
import com.backend.dream.dto.VoucherTypeDTO;
import com.backend.dream.entity.Voucher;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface VoucherService {
    List<VoucherDTO> getApplicableVouchers(String username);
    List<VoucherDTO> getAllVouchers();
    // Display the voucher status in combobox
    List<VoucherStatusDTO> getAllVoucherStatus();
    List<VoucherDTO> getVouchersByStatusId(Long statusId);
    List<VoucherDTO> searchVouchersByName(String name);
    void delete(Long id);
    List<Voucher> createVoucher(JsonNode voucherData);
    void deleteByNumberAndType(String name, Long idType);
    VoucherDTO updateVoucher(VoucherDTO voucherDTO, Long id);
    List<VoucherDTO> updateListVoucherByNameAndIdType(VoucherDTO voucherDTO, String name, Long idType);
    VoucherDTO getVoucherByID(Long voucherID);
    List<VoucherDTO> getVouchersByNameAndType(String name, Long idType);

    ByteArrayInputStream getdataVoucher() throws IOException;
}

package com.backend.dream.service.imp;

import com.backend.dream.dto.AccountDTO;
import com.backend.dream.dto.VoucherDTO;
import com.backend.dream.dto.VoucherStatusDTO;
import com.backend.dream.entity.Voucher;
import com.backend.dream.entity.VoucherStatus;
import com.backend.dream.entity.VoucherType;
import com.backend.dream.mapper.VoucherMapper;
import com.backend.dream.mapper.VoucherStatusMapper;
import com.backend.dream.repository.AccountRepository;
import com.backend.dream.repository.VoucherRepository;
import com.backend.dream.repository.VoucherStatusRepository;
import com.backend.dream.service.AccountService;
import com.backend.dream.service.VoucherService;
import com.backend.dream.util.ExcelUltils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Service
public class VoucherServiceImp implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherMapper voucherMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    VoucherStatusRepository voucherStatusRepository;

    @Override
    public List<VoucherDTO> getApplicableVouchers(String username) {
        Long userId = accountService.findIDByUsername(username);

        List<Voucher> applicableVouchers = voucherRepository.findApplicableVouchers(userId);
        return applicableVouchers.stream()
                .map(voucherMapper::voucherToVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> getAllVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        deleteExpiredVouchers();
        return vouchers.stream()
                .map(VoucherMapper.INSTANCE::voucherToVoucherDTO)
                .collect(Collectors.toList());
    }

    private void deleteExpiredVouchers(){
        List<VoucherDTO> expiredVouchers = voucherRepository.findByExpiredDateBefore(new Date()).stream().map(voucherMapper::voucherToVoucherDTO).collect(Collectors.toList());
        voucherRepository.deleteAll(voucherMapper.listVoucherDTOToListVoucher(expiredVouchers));
    }

    @Override
    public List<VoucherStatusDTO> getAllVoucherStatus() {
        List<VoucherStatus> voucherStatusList = voucherStatusRepository.findAll();
        return voucherStatusList.stream()
                .map(VoucherStatusMapper.INSTANCE::voucherStatusToVoucherStatusDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> getVouchersByStatusId(Long statusId) {
        List<Voucher> vouchers = voucherRepository.findByStatusId(statusId);
        return vouchers.stream()
                .map(voucherMapper::voucherToVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> searchVouchersByName(String name) {
        List<Voucher> vouchers = voucherRepository.searchByName(name);
        return vouchers.stream()
                .map(voucherMapper::voucherToVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public List<Voucher> createVoucher(JsonNode voucherData) {
        ObjectMapper mapper = new ObjectMapper();
        VoucherDTO voucherDTO = mapper.convertValue(voucherData, VoucherDTO.class);

        if (voucherDTO.getType() == 1L) {
            return Collections.singletonList(createSingleVoucher(voucherDTO));
        } else {
            return createMultipleVouchers(voucherDTO);
        }
    }

    private Voucher createSingleVoucher(VoucherDTO voucherDTO) {
        Voucher voucher = voucherMapper.voucherDTOToVoucher(voucherDTO);
        voucher.setAccount(null);
        return voucherRepository.save(voucher);
    }

    private List<Voucher> createMultipleVouchers(VoucherDTO voucherDTO) {
        List<AccountDTO> allAccounts = accountService.getAllAccounts();

        List<VoucherDTO> vouchers = allAccounts.stream().map(account -> {
            VoucherDTO newVoucherDTO = new VoucherDTO();
            newVoucherDTO.setName(voucherDTO.getName());
            newVoucherDTO.setNumber(voucherDTO.getNumber());
            newVoucherDTO.setCreateDate(voucherDTO.getCreateDate());
            newVoucherDTO.setExpiredDate(voucherDTO.getExpiredDate());
            newVoucherDTO.setPrice(voucherDTO.getPrice());
            newVoucherDTO.setCondition(voucherDTO.getCondition());
            newVoucherDTO.setIcon(voucherDTO.getIcon());
            newVoucherDTO.setStatus(voucherDTO.getStatus());
            newVoucherDTO.setType(voucherDTO.getType());
            newVoucherDTO.setId_account(account.getId());
            return newVoucherDTO;
        }).collect(Collectors.toList());

        List<Voucher> savedVouchers = voucherRepository.saveAll(voucherMapper.listVoucherDTOToListVoucher(vouchers));

        return savedVouchers;
    }
    @Override
    public VoucherDTO updateVoucher(VoucherDTO voucherDTO, Long id) {
        Voucher voucher = voucherRepository.findById(id).get();

        VoucherType type = new VoucherType();
        type.setId(voucherDTO.getType());


        voucher.setName(voucherDTO.getName());
        voucher.setNumber(voucherDTO.getNumber());
        voucher.setIcon(voucherDTO.getIcon());
        voucher.setCondition(voucherDTO.getCondition());
        voucher.setPrice(voucherDTO.getPrice());
        voucher.setExpiredDate(voucherDTO.getExpiredDate());
        voucher.setType(type);

        Voucher voucherUpdate = voucherRepository.save(voucher);

        return voucherMapper.voucherToVoucherDTO(voucherUpdate);
    }

    @Override
    public List<VoucherDTO> updateListVoucherByNameAndIdType(VoucherDTO voucherDTO, String number, Long idType) {
        List<Voucher> voucherList = voucherRepository.findListVouchersByNumberAndIDType(number, idType);
        VoucherType type = new VoucherType();
        type.setId(voucherDTO.getType());

        voucherList.forEach(voucher ->{
            voucher.setName(voucherDTO.getName());
            voucher.setNumber(voucherDTO.getNumber());
            voucher.setIcon(voucherDTO.getIcon());
            voucher.setCondition(voucherDTO.getCondition());
            voucher.setPrice(voucherDTO.getPrice());
            voucher.setExpiredDate(voucherDTO.getExpiredDate());
            voucher.setType(type);
        });
        List<Voucher> vouchersUpdate = voucherRepository.saveAll(voucherList);

        return vouchersUpdate.stream().map(voucherMapper::voucherToVoucherDTO).collect(Collectors.toList());
    }

    @Override
    public ByteArrayInputStream getdataVoucher() throws IOException {
        List<Voucher> vouchers = voucherRepository.findAll();
        ByteArrayInputStream data = ExcelUltils.dataToExcel(vouchers, ExcelUltils.SHEET_NAMEVOUCHER,ExcelUltils.HEADERVOUCHER);
        return data;
    }



    @Override
    public VoucherDTO getVoucherByID(Long voucherID) {
        Voucher voucher = voucherRepository.findById(voucherID).orElse(null);
        return voucherMapper.voucherToVoucherDTO(voucher);
    }

    @Override
    public List<VoucherDTO> getVouchersByNameAndType(String number, Long idType) {
        List<Voucher> vouchers = voucherRepository.findListVouchersByNumberAndIDType(number, idType);
        return vouchers.stream()
                .map(voucherMapper::voucherToVoucherDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteByNumberAndType(String name, Long idType) {
        List<VoucherDTO> vouchers = voucherRepository.findListVouchersByNumberAndIDType(name,idType).stream().map(voucherMapper::voucherToVoucherDTO).collect(Collectors.toList());
        voucherRepository.deleteAll(voucherMapper.listVoucherDTOToListVoucher(vouchers));
    }


}
package com.backend.dream.service.imp;

import com.backend.dream.dto.VoucherTypeDTO;
import com.backend.dream.mapper.VoucherTypeMapper;
import com.backend.dream.repository.VoucherTypeRepository;
import com.backend.dream.service.VoucherTypeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Service
public class VoucherTypeServiceImp implements VoucherTypeService {
    @Autowired
    private VoucherTypeRepository voucherTypeRepository;
    @Autowired
    private VoucherTypeMapper voucherTypeMapper;

    @Override
    public List<VoucherTypeDTO> getAllVoucherTypes() {
        return voucherTypeRepository.findAll().stream().map(voucherTypeMapper::voucherTypeToVoucherTypeDTO).collect(Collectors.toList());
    }
}

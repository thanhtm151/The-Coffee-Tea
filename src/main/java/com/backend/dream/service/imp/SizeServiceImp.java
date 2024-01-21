package com.backend.dream.service.imp;

import com.backend.dream.dto.SizeDTO;
import com.backend.dream.entity.Size;
import com.backend.dream.mapper.SizeMapper;
import com.backend.dream.repository.SizeRepository;
import com.backend.dream.service.SizeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public class SizeServiceImp implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private SizeMapper sizeMapper;
    @Override
    public List<SizeDTO> findAll() throws Exception {
        List<Size> listsize = sizeRepository.findAll();
        return sizeMapper.listSizeToListSizeDTO(listsize);
    }
}

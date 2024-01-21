package com.backend.dream.service;

import com.backend.dream.dto.SizeDTO;

import java.util.List;

public interface SizeService {
    List<SizeDTO> findAll() throws Exception;
}

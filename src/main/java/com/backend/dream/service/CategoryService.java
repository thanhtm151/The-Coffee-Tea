package com.backend.dream.service;


import com.backend.dream.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
    CategoryDTO findById(Long id);
    CategoryDTO create(CategoryDTO categoryDTO);
    CategoryDTO update(CategoryDTO categoryDTO);
    void delete(Long id);
    CategoryDTO getDiscountByCategoryId(Long id);
}

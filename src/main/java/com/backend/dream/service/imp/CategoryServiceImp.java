package com.backend.dream.service.imp;

import com.backend.dream.dto.CategoryDTO;
import com.backend.dream.entity.Category;
import com.backend.dream.mapper.CategoryMapper;
import com.backend.dream.repository.CategoryRepository;
import com.backend.dream.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Transactional
@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private  CategoryMapper categoryMapper;

    @Override
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category != null ? categoryMapper.categoryToCategoryDTO(category) : null;
    }

    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        if(categoryDTO.getId_discount() == null){
            category.setDiscount(null);
        }
        Category createdCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDTO(createdCategory);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        if(categoryDTO.getId_discount() == null){
            category.setDiscount(null);
        }
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDTO(updatedCategory);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO getDiscountByCategoryId(Long idCategory) {
        Optional<Category> optionalCategory = categoryRepository.findByIDCategory(idCategory);
        return optionalCategory.map(categoryMapper::categoryToCategoryDTO).orElse(null);
    }


    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryDTO)
                .collect(Collectors.toList());
    }
}

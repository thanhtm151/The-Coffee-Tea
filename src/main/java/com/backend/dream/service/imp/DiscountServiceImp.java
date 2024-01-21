package com.backend.dream.service.imp;

import com.backend.dream.dto.CategoryDTO;
import com.backend.dream.dto.DiscountDTO;
import com.backend.dream.entity.Discount;
import com.backend.dream.mapper.CategoryMapper;
import com.backend.dream.mapper.DiscountMapper;
import com.backend.dream.repository.CategoryRepository;
import com.backend.dream.repository.DiscountRepository;
import com.backend.dream.service.CategoryService;
import com.backend.dream.service.DiscountService;
import com.backend.dream.util.ExcelUltils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class DiscountServiceImp implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private DiscountMapper discountMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        Discount discount = discountMapper.discountDTOToDiscount(discountDTO);
        Discount createdDiscount = discountRepository.save(discount);
        return discountMapper.discountToDiscountDTO(createdDiscount);
    }

    // @Override
    // public void deleteDiscount(Long id) {
    // discountRepository.deleteById(id);
    // }

    @Override
    public List<DiscountDTO> findAll() {
        List<Discount> products = discountRepository.findAll();
        return products.stream().map(discountMapper::discountToDiscountDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDTO update(DiscountDTO discountDTO) {
        Discount discount = discountMapper.discountDTOToDiscount(discountDTO);
        Discount updatedDiscount = discountRepository.save(discount);
        return discountMapper.discountToDiscountDTO(updatedDiscount);
    }

    @Override
    public void delete(Long id) {
        discountRepository.deleteById(id);
    }

    @Override
    public ByteArrayInputStream getdataDiscount() throws IOException {
        List<Discount> discounts = discountRepository.findAll();
        ByteArrayInputStream data = ExcelUltils.dataToExcel(discounts, ExcelUltils.SHEET_NAMEDISCOUNT,
                ExcelUltils.HEADERDISCOUNT);
        return data;
    }

    @Override
    public Double getDiscountPercentByProductId(Long idCategory) {
        CategoryDTO categoryDTO = categoryService.getDiscountByCategoryId(idCategory);
        if (categoryDTO != null) {
            return categoryDTO.getPercent_discount();
        }
        return 0.0;
    }

    @Override
    public DiscountDTO getDiscountByID(Long id) {
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        return optionalDiscount.map(discountMapper::discountToDiscountDTO).orElse(null);
    }

    @Override
    public List<DiscountDTO> searchDiscountByName(String name) {
        List<Discount> discount = discountRepository.searchByName(name);
        return discount.stream().map(discountMapper::discountToDiscountDTO)
                .collect(Collectors.toList());
    }

}

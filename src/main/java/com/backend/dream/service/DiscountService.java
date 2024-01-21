package com.backend.dream.service;

import com.backend.dream.dto.CategoryDTO;
import com.backend.dream.dto.DiscountDTO;
import com.backend.dream.dto.ProductDTO;
import com.backend.dream.entity.Discount;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface DiscountService {
    DiscountDTO createDiscount(DiscountDTO discount);
    Double getDiscountPercentByProductId(Long idProduct);
//    void deleteDiscount(Long discountId);
    List<DiscountDTO> findAll();
    DiscountDTO update(DiscountDTO discountDTO);
    void delete(Long id);

    DiscountDTO getDiscountByID(Long id);

    List<DiscountDTO> searchDiscountByName(String name);

    ByteArrayInputStream getdataDiscount() throws IOException;

}

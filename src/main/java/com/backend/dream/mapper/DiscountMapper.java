package com.backend.dream.mapper;

import com.backend.dream.dto.DiscountDTO;
import com.backend.dream.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);
    DiscountDTO discountToDiscountDTO(Discount discount);
    Discount discountDTOToDiscount(DiscountDTO discountDTO);
}

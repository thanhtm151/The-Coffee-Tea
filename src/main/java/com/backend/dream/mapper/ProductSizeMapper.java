package com.backend.dream.mapper;

import com.backend.dream.dto.CategoryDTO;
import com.backend.dream.dto.ProductDTO;
import com.backend.dream.dto.ProductSizeDTO;
import com.backend.dream.entity.Category;
import com.backend.dream.entity.Product;
import com.backend.dream.entity.ProductSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductSizeMapper {
    ProductSizeMapper INSTANCE = Mappers.getMapper(ProductSizeMapper.class);

    @Mapping(source = "size.id",target = "id_size")
    @Mapping(source = "product.id",target = "id_product")
    @Mapping(source = "product.image",target = "image")
    @Mapping(source = "product.name",target = "name")
    @Mapping(source = "product.category.discount.percent",target = "discount_percent")
    @Mapping(source = "size.name",target = "name_size")
    ProductSizeDTO productSizeToProductSizeDTO(ProductSize productSize);

    @Mapping(source = "id_size", target = "size.id")
    @Mapping(source = "id_product", target = "product.id")
    ProductSize productSizeDTOToProductSize(ProductSizeDTO productSizeDTO);

    ProductDTO map(Product product);

    CategoryDTO map(Category category);
}

package com.backend.dream.mapper;

import com.backend.dream.dto.ProductDTO;
import com.backend.dream.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.name", target = "name_category")
    @Mapping(source = "category.id", target = "id_category")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "category.name", target = "category")
    List<ProductDTO> productsToProductDTOs(List<Product> product);

    @Mapping(source = "id_category", target = "category.id")
    Product productDTOToProduct(ProductDTO product);

}

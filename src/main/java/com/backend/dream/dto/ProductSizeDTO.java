package com.backend.dream.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSizeDTO {
    private Long id;

    @NotNull(message = "Please choose at least one ")
    private Long id_product;

    @NotNull(message = "Please choose at least one ")
    private Long id_size;

    private String name_size;

    @NotNull(message = "Price is required")
    @Digits(integer = 6, fraction = 2, message = "Price must have up to 6 digits in total, with up to 2 decimal places")
    @Positive(message = "Price must be a positive number")
    @Min(value = 1, message = "Price must be grater than 1")
    @Max(value = 999999, message = "Price must not exceed 999999")
    private Double price;

    private String image;

    private String name;

    private Double discount_percent;

}

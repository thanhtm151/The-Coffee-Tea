package com.backend.dream.dto;

import com.backend.dream.entity.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits in total, with up to 2 decimal places")
    @Positive(message = "Price must be a positive number")
    @Min(value = 1, message = "Price must be grater than 1")
    @Max(value = 999999, message = "Price must not exceed 999999")
    private Double price;

    @NotBlank(message = "Image is required")
    private String image;

    @NotBlank(message = "Describe is required")
    private String describe;

    @NotNull(message = "Please choose at least one")
    private Boolean active;

    private String name_category;

    @NotNull(message = "Please choose at least one ")
    private Long id_category;

    @NotNull(message = "Please provide a date")
    private Date createDate = new Date();

    public String getFormattedPrice() {
        DecimalFormat df = new DecimalFormat("#,### ₫");
        return df.format(price);
    }

    private Double discountedPrice;

    public String getFormattedDiscountedPrice() {
        if (discountedPrice != null) {
            DecimalFormat df = new DecimalFormat("#,### ₫");
            return df.format(discountedPrice);
        } else {
            return "";
        }
    }

    private Boolean isDiscounted;

    private List<SizeDTO> availableSizes;

    private Size selectedSize;

    private Long selectedSizeId;

    private Double averageRating;

}

package com.backend.dream.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DiscountDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Number is required")
    private String number;

    @NotNull(message = "Percent is required")
    @Digits(integer = 5, fraction = 2, message = "Percent must have up to 5 digits in total, with up to 2 decimal places")
    @Positive(message = "Percent must be a positive number")
    @Max(value = 999999, message = "Percent must not exceed 999999")
    private Double percent;

    @NotNull(message = "Please provide a date")
    private Date activeDate = new Date();

    @NotNull(message = "Please provide a date")
    @Future(message = "Please choose a date in the future")
    private Date expiredDate = new Date();

    @NotNull(message = "Please choose at least one")
    private boolean active;


}

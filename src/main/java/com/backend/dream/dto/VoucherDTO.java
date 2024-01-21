package com.backend.dream.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Number is required")
    private String number;

    private Date createDate = new Date();

    @NotNull(message = "Please provide a date")
    @Future(message = "Please choose a date in the future")
    private Date expiredDate;

    @NotNull(message = "Price is required")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits in total, with up to 2 decimal places")
    @Positive(message = "Price must be a positive number")
    @Min(value = 1000, message = "Price must be grater than 1000")
    @Max(value = 999999, message = "Price must not exceed 999999")
    private Double price;

    @NotNull(message = "Percent is required")
    @Digits(integer = 5, fraction = 2, message = "Percent must have up to 5 digits in total, with up to 2 decimal places")
    @Positive(message = "Percent must be a positive number")
    @Min(value = 1000, message = "Percent must be grater or equal than  1000")
    @Max(value = 999999, message = "Percent must not exceed 999999")
    private Double condition;

    @NotBlank(message = "Please choose at least one")
    private String icon;

    @NotNull(message = "Please choose at least one ")
    @Min(value = 1, message = "Please choose at least one")
    private Long status;

    @NotNull(message = "Please choose at least one ")
    @Min(value = 1, message = "Please choose at least one")
    private Long type;

    private Long id_account;
}

package com.backend.dream.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private Long id_discount;

    private String name_discount;

    private Double percent_discount;
}

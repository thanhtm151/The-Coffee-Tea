package com.backend.dream.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetailDTO {
    private Long id;

    private int quantity;

    private double price;

    private Long id_order;

    private Long id_product;

    private String account_fullname;

    private String account_phone;

    private Date order_createDate;

    private String order_address;

    private Long order_status;

    private String product_image;

    private String product_name;

    private Long id_size;

    private Double discount;

    private Double distance;
}

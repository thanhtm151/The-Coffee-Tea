package com.backend.dream.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {
    private Long id;

    @NotBlank(message = "Address is required")
    private String address;

    private String note;

    private Date createDate;

    private Time createTime;

    private Double distance;

    private Long status;

    private int id_account;

    private String fullname;

    private Long id_voucher;

    private String name_voucher;

    private String qr;

    private List<OrderDetailDTO> orderDetailsDTO;

    private Double totalAmount;
    public String getFormattedPrice() {
        if(totalAmount != null && distance!= null){
        DecimalFormat df = new DecimalFormat("#,###₫");
        return df.format(totalAmount + distance * 4 );
        } else {
            return "";
        }
    }
}

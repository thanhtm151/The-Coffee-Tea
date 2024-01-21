package com.backend.dream.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class FeedBackDTO {
    private Long id;

    private String note;

    private int rating;

    private String image;

    private Long id_account;

    private Long id_product;

    private Date createDate;

    private Time createTime;

    private AccountDTO accountDTO;
}
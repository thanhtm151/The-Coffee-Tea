package com.backend.dream.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class TokenDTO {
    private Long id;

    private String token;

    private String tokenType;

    private boolean active;

    private LocalDateTime expiredDate;

    private Long id_account;
}

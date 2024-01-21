package com.backend.dream.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AccountsLockDTO {
    private Long id;
    private Long accountId;
    private String reason;
    private Timestamp banDate;
}

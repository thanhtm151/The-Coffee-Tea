package com.backend.dream.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="token")
public class Token implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "tokentype")
    private String tokenType;

    private boolean active;
    @Column(name = "expireddate")
    private LocalDateTime expiredDate;

    @ManyToOne
    @JoinColumn(name = "idaccount")
    private Account account;
}

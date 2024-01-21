package com.backend.dream.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Voucher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String number;

    @Column(name = "createdate")
    @Temporal(TemporalType.DATE)
    private Date createDate = new Date();

    @Column(name = "expireddate")
    @Temporal(TemporalType.DATE)
    private Date expiredDate;

    private Double price;

    private Double condition;

    private String icon;

    @JsonIgnore
    @OneToMany(mappedBy = "voucher")
    private List<Orders> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idaccount")
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JoinColumn(name = "idvoucherstatus")
    private VoucherStatus status;

    @ManyToOne
    @JoinColumn(name = "idvouchertype")
    private VoucherType type;
}

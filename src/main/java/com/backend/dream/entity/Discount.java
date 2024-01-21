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
public class Discount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discountname")
    private String name;

    @Column(name = "discountnumber")
    private String number;

    @Column(name = "discountpercent")
    private Double percent;

    private boolean active;

    @Column(name = "activedate")
    @Temporal(TemporalType.DATE)
    private Date activeDate=new Date();

    @Column(name = "expireddate")
    @Temporal(TemporalType.DATE)
    private Date expiredDate=new Date();

    @JsonIgnore
    @OneToMany(mappedBy = "discount")
    private List<Category> category;


}

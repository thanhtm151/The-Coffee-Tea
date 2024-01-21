package com.backend.dream.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    @Column(unique = false)
    private String avatar;

    private String firstname;

    private String lastname;

    private String fullname;

    private String phone;

    private String address;

    private boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Authority> authority;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Voucher> voucher;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Orders> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Token> token;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<FeedBack> feedback;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

}

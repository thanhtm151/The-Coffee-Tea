    package com.backend.dream.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.io.Serializable;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Entity
    @Table(name = "\"productsize\"")
    public class ProductSize implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Double price;

        @ManyToOne
        @JoinColumn(name = "idproduct")
        private Product product;

        @ManyToOne
        @JoinColumn(name = "idsize")
        private Size size;

    }

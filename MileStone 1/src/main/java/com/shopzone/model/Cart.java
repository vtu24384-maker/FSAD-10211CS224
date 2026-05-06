package com.shopzone.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart", uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "product_id"}))
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "session_id")
    private String sessionId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity = 1;
}

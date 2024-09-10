package com.enigmacamp.springbootwmbreview.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_order_detail")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
//    @JsonIgnore //ini dia supaaya ga panggil terus terusan
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "price", columnDefinition = "BIGINT CHECK (price > 0)")
    private Long price;

    @Column(name = "quantity", columnDefinition = "INT CHECK (quantity > 0)")
    private Integer quantity;
}

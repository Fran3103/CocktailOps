package com.cocktailops.CocktailOps.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private  Integer guests;

    @Column(nullable = false)
    private  Integer drinksPerPerson;

    @Column(nullable = false)
    private  Integer durationHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderMode mode = OrderMode.TIME;

    @Column(name = "total_drinks", nullable = false)
    private Integer totalDrinks;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderCocktail> Cocktails = new ArrayList<>();

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }


}

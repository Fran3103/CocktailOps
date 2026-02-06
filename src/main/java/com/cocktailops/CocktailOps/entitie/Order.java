package com.cocktailops.CocktailOps.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
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

    private  Integer guests;

    private  Integer drinksPerPerson;

    private  Integer durationHours;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cocktail_id", nullable = false)
    private Cocktail cocktail;

    private String status;

    private List<OrderItem> orderItems;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }


}

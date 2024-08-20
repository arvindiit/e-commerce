package org.arvind.commerce.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
    private Long customerId;
    private boolean paymentDone;
    private double price;
    private LocalDateTime createdAt;
    private boolean paid;

    public Orders() {
        paid = false;
    }

    public List<Item> getItems() {
        if(items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
}

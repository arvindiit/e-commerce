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
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
    @Column(unique = true)
    private String ref;
    private double price;
    private LocalDateTime createdAt;

    public List<Item> getItems() {
        if(items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
}

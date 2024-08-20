package org.arvind.commerce.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Item implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private long productId;
    private int quantity;
    private boolean ordered;
    private LocalDateTime createdAt;

    public Item(){
        ordered = false;
        createdAt = LocalDateTime.now();
    }
    @Override
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

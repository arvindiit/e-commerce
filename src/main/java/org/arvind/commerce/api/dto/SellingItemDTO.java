package org.arvind.commerce.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class SellingItemDTO {
    private String name;
    private Long soldQuantity;
    private Long remainingQuantity;
    private double price;
    private boolean low;

    public SellingItemDTO(String name, double price, Long soldQuantity, Long remainingQuantity) {
        this.name = name;
        this.low = remainingQuantity <= 10;
        this.remainingQuantity = remainingQuantity;
        this.soldQuantity = soldQuantity;
        this.price = price;
    }
}

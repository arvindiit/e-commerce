package org.arvind.commerce.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {

    @NotNull
    private Long productId;
    @Min(1)
    private int quantity;
}

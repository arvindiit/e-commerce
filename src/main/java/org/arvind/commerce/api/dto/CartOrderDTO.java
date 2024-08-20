package org.arvind.commerce.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartOrderDTO {

    @NotEmpty
    private Long cartId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String emailAddress;
}

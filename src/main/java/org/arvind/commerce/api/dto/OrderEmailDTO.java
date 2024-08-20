package org.arvind.commerce.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEmailDTO {

    long orderId;
    String emailAddress;
    String paymentLink;
}

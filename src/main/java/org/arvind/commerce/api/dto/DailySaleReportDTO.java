package org.arvind.commerce.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DailySaleReportDTO {

    String name;
    LocalDate date;
    double saleAmount;
}

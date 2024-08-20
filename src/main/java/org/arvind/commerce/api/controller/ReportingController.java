package org.arvind.commerce.api.controller;

import org.arvind.commerce.api.dto.DailySaleReportDTO;
import org.arvind.commerce.api.dto.SellingItemDTO;
import org.arvind.commerce.api.service.ReportingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }


    @GetMapping("/top5SellingProduct")
    public List<SellingItemDTO> top5SellingProduct() {

        return reportingService.getTopSellingProduct(5);
    }
    @GetMapping("/leastSellingProduct")
    public SellingItemDTO leastSellingProduct() {
        return reportingService.getBottomSellingProduct(1);
    }

    @GetMapping("/dailySalesReport")
    public List<DailySaleReportDTO> dailySalesReport(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate")LocalDate endDate) {
        return reportingService.dailySaleReport(startDate, endDate);
    }
}

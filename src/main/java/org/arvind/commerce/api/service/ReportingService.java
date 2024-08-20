package org.arvind.commerce.api.service;

import lombok.extern.slf4j.Slf4j;
import org.arvind.commerce.api.dto.DailySaleReportDTO;
import org.arvind.commerce.api.dto.SellingItemDTO;
import org.arvind.commerce.api.exception.ResourceNotFoundException;
import org.arvind.commerce.api.model.Product;
import org.arvind.commerce.api.repo.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportingService {

    private final ItemRepository itemRepository;
    private final ProductService productService;

    public ReportingService(ItemRepository itemRepository, ProductService productService) {
        this.itemRepository = itemRepository;
        this.productService = productService;
    }

    public List<DailySaleReportDTO> dailySaleReport(LocalDate startDate, LocalDate endDate) {
        List<Object[]> list = itemRepository.findProductQuantityForADateRange(LocalDateTime.of(startDate, LocalTime.MIDNIGHT), LocalDateTime.of(endDate.plusDays(1), LocalTime.MIDNIGHT));
        List<DailySaleReportDTO> dailySaleReportDTOS = new ArrayList<>();
        list.forEach(x -> {
            String productName = (String) x[0];
            Product product = productService.getProductByName(productName);
            dailySaleReportDTOS.add(new DailySaleReportDTO(productName, (LocalDate) x[1], product.getPrice() * (long)x[2]));
        });

        return dailySaleReportDTOS;
    }

    public List<SellingItemDTO> getTopSellingProduct(int limit) {
        LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime tomMidnight = todayMidnight.plusDays(1);
        List<Object[]> list = itemRepository.findTopSellingProductForADateRange(todayMidnight, tomMidnight, true, limit);
        List<SellingItemDTO> sellingItemDTOList = new ArrayList<>();

        list.forEach(x -> {
            String productName = (String) x[0];
            Product product = productService.getProductByName(productName);
            sellingItemDTOList.add(new SellingItemDTO(productName, product.getPrice(), (Long) x[1], product.getQuantity()));
        });
        return sellingItemDTOList;
    }

    public SellingItemDTO getBottomSellingProduct(int limit) {
        LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime tomMidnight = todayMidnight.plusDays(1);
        List<Object[]> list = itemRepository.findBottomSellingProductForADateRange(todayMidnight, tomMidnight, true, limit);
        if(list == null || list.isEmpty()) {
            throw new ResourceNotFoundException("No Order found for today");
        }
        Object[] objectArr = list.get(0);
        String productName = (String) objectArr[0];
        Product product = productService.getProductByName(productName);
        return new SellingItemDTO(productName, product.getPrice(), (Long) objectArr[1], product.getQuantity());
    }
}

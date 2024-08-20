package org.arvind.commerce.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.arvind.commerce.api.AssignmentApplication;
import org.arvind.commerce.api.dto.SellingItemDTO;
import org.arvind.commerce.api.model.Orders;
import org.arvind.commerce.api.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingJourneyTest {

    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper mapper;

    @BeforeAll
    static void setup(){
        AssignmentApplication.backupQueue = new LinkedList<>();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testShoppingJourney() throws Exception {

        long cartId = createCartTest();
        testAddItems(cartId);
        long orderId = createOrderTest(cartId);
        testGetOrder(orderId);
        MvcResult result = mockMvc.perform(get("/api/cart/"+cartId)).andExpect(status().is4xxClientError()).andReturn();
        Assertions.assertEquals("Cart with identifier "+cartId+" not found", result.getResponse().getContentAsString());
        testTop5SellingProduct();
        testLeastSellingProduct();
        testDailySalesReport();
        testDeleteOrder(orderId);

    }

    private long createCartTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/cart/new")).andExpect(status().isOk()).andReturn();

        return Long.parseLong(result.getResponse().getContentAsString());
    }

    void testAddItems(long cartId) throws Exception {

        mockMvc.perform(post("/api/cart/"+cartId+"/addItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\": 1,\n" +
                        "\"quantity\": 10}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.items", hasSize(1)));

        mockMvc.perform(post("/api/cart/"+cartId+"/addItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 2,\n" +
                                "\"quantity\": 20}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.items", hasSize(2)));
    }

    private long createOrderTest(long cartId) throws Exception {

        MvcResult result = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cartId\": "+cartId+",\n" +
                                "\"name\": \"test\", " +
                                "\"emailAddress\": \"test@test.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        return Long.parseLong(result.getResponse().getContentAsString());

    }

    void testGetOrder(long orderId) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/order/"+orderId))
                .andExpect(status().isOk()).andReturn();
        Orders order = mapper.readValue(result.getResponse().getContentAsString(), Orders.class);
        Assertions.assertEquals(2, order.getItems().size());
        AtomicReference<Double> orderPrice = new AtomicReference<>((double) 0);
        order.getItems().forEach(x -> {
            MvcResult resultP = null;
            try {
                resultP = mockMvc.perform(get("/api/product/"+x.getProductId()))
                        .andExpect(status().isOk()).andReturn();
                Product product = mapper.readValue(resultP.getResponse().getContentAsString(), Product.class);
                Assertions.assertEquals(x.getName(), product.getName());
                orderPrice.set(orderPrice.get() + x.getQuantity() * product.getPrice());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        Assertions.assertEquals(orderPrice.get(), order.getPrice());
    }

    void testTop5SellingProduct() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/report/top5SellingProduct"))
                .andExpect(status().isOk()).andReturn();
        List sellingItemDTOS = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        Map<String, Object> top1 = (Map<String, Object>) sellingItemDTOS.get(0);
        Map<String, Object> top2 = (Map<String, Object>) sellingItemDTOS.get(1);

        MvcResult resultTop = mockMvc.perform(get("/api/product/2"))
                .andExpect(status().isOk()).andReturn();
        Product product = mapper.readValue(resultTop.getResponse().getContentAsString(), Product.class);

        Assertions.assertEquals(product.getName(), top1.get("name"));
        Assertions.assertEquals(product.getPrice(), top1.get("price"));

        MvcResult resultTop2 = mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk()).andReturn();
        Product product2 = mapper.readValue(resultTop2.getResponse().getContentAsString(), Product.class);

        Assertions.assertEquals(product2.getName(), top2.get("name"));
        Assertions.assertEquals(product2.getPrice(), top2.get("price"));
    }

    void testLeastSellingProduct() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/report/leastSellingProduct"))
                .andExpect(status().isOk()).andReturn();
        SellingItemDTO sellingItemDTOS = mapper.readValue(result.getResponse().getContentAsString(), SellingItemDTO.class);

        MvcResult resultTop = mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk()).andReturn();
        Product product = mapper.readValue(resultTop.getResponse().getContentAsString(), Product.class);

        Assertions.assertEquals(product.getName(), sellingItemDTOS.getName());
        Assertions.assertEquals(product.getPrice(), sellingItemDTOS.getPrice());
    }

    void testDailySalesReport() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/report/dailySalesReport?startDate="+ LocalDate.now() + "&endDate="+LocalDate.now()))
                .andExpect(status().isOk()).andReturn();
        List dailySaleReportDTO = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        Map<String, Object> top1 = (Map<String, Object>) dailySaleReportDTO.get(0);
        Map<String, Object> top2 = (Map<String, Object>) dailySaleReportDTO.get(1);

        String productName = (String) top1.get("name");

        MvcResult resultTop = mockMvc.perform(get("/api/product?name="+productName))
                .andExpect(status().isOk()).andReturn();
        Product product = mapper.readValue(resultTop.getResponse().getContentAsString(), Product.class);

        Assertions.assertEquals(product.getName(), top1.get("name"));
        Assertions.assertEquals(product.getPrice() * 10, top1.get("saleAmount"));

        String productName2 = (String) top2.get("name");

        MvcResult resultTop2 = mockMvc.perform(get("/api/product?name="+productName2))
                .andExpect(status().isOk()).andReturn();
        Product product2 = mapper.readValue(resultTop2.getResponse().getContentAsString(), Product.class);

        Assertions.assertEquals(product2.getName(), top2.get("name"));
        Assertions.assertEquals(product2.getPrice() * 20, top2.get("saleAmount"));
    }

    void testDeleteOrder(long orderId) throws Exception {
        mockMvc.perform(delete("/api/order/remove/"+orderId))
                .andExpect(status().isOk());
    }

}

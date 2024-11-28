package com.example.controller;

import com.example.model.OrderInfoRepository;
import com.example.model.ProductRepository;
import com.example.model.ShopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    OrderInfoRepository orderInfoRepository;
    @MockBean
    ShopRepository shopRepository;
    @MockBean
    ProductRepository productRepository;

    // Scenario 1: Accessing the payment view
    @Test
    public void testOpenPaymentView_ReturnsPaymentView() throws Exception {
        mockMvc.perform(get("/checkout"))
                .andExpect(status().isOk())
                .andExpect(view().name("shopCheckout"));
    }
}

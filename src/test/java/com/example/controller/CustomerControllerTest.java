package com.example.controller;

import com.example.model.Customer;
import com.example.model.CustomerRepository;
import com.example.model.Shop;
import com.example.model.ShopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private ShopRepository shopRepository;

    // 1. Tests for GET /create-customer

    @Test
    public void testShowCreateCustomerForm_ReturnsCreateCustomerView() throws Exception {
        mockMvc.perform(get("/create-customer"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("Customer"))
                .andExpect(view().name("createCustomer"));
    }

    // 2. Tests for POST /create-customer

    @Test
    public void testCreateShopper_RedirectsToShopperScreen() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/create-customer")
                        .flashAttr("Customer", customer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/shopper?customerId=1"));

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testCreateShopper_MissingCustomerData_ReturnsError() throws Exception {
        Customer incompleteCustomer = new Customer();  // No required fields set

        mockMvc.perform(post("/create-customer")
                        .flashAttr("Customer", incompleteCustomer))
                .andExpect(status().isBadRequest());

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateShopper_DatabaseSaveFails() throws Exception {
        Customer customer = new Customer();

        // Simulate a database failure by throwing an exception
        when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(post("/create-customer")
                        .flashAttr("Customer", customer))
                .andExpect(status().is5xxServerError());  // Expecting a 500-series error


        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    // 3. Tests for GET /shopper

    @Test
    public void testOpenCustomerScreen_WithShops_ReturnsCustomerScreenView() throws Exception {
        Shop shop = new Shop();
        shop.setName("Test Shop");
        List<Shop> shops = Collections.singletonList(shop);

        when(shopRepository.findAll()).thenReturn(shops);

        mockMvc.perform(get("/shopper"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shops"))
                .andExpect(model().attribute("shops", shops))
                .andExpect(view().name("customerScreen"));

        verify(shopRepository, times(1)).findAll();
    }

    @Test
    public void testOpenCustomerScreen_NoShops_ReturnsEmptyShopList() throws Exception {
        // Simulate empty shop list
        when(shopRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/shopper"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shops"))
                .andExpect(model().attribute("shops", Collections.emptyList()))
                .andExpect(view().name("customerScreen"));

        verify(shopRepository, times(1)).findAll();
    }

    @Test
    public void testOpenCustomerScreen_DatabaseError() throws Exception {
        when(shopRepository.findAll()).thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(get("/shopper"))
                .andExpect(status().is5xxServerError());

        verify(shopRepository, times(1)).findAll();
    }
}

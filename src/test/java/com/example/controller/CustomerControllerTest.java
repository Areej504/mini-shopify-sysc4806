package com.example.controller;

import com.example.model.*;
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

    // Tests for GET /create-customer

    @Test
    public void testShowCreateCustomerForm_ReturnsCreateCustomerView() throws Exception {
        mockMvc.perform(get("/create-customer"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("Customer"))
                .andExpect(view().name("createCustomer"));
    }

    // Tests for POST /create-customer
    @Test
    public void testCreateCustomer_Success() throws Exception {
        // test case
        String email = "test@example.com";
        String password = "password123";
        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(1L);
        savedCustomer.setEmail(email);
        savedCustomer.setPassword(password);

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/create-customer")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer-login"));

        // Verify repository interactions
        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerRepository, times(1)).save(argThat(customer ->
                customer.getEmail().equals(email) && customer.getPassword().equals(password)
        ));
    }

    @Test
    public void testCreateCustomer_EmailAlreadyExists() throws Exception {
        // test case
        String email = "existing@example.com";
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(new Customer()));

        mockMvc.perform(post("/create-customer")
                        .param("email", email)
                        .param("password", "password123"))
                .andExpect(status().isOk()) // Renders the same form
                .andExpect(view().name("createCustomer"))
                .andExpect(model().attribute("errorMessage", "Email already exists"));

        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerRepository, never()).save(any(Customer.class)); // Ensure no save occurs
    }

    // Tests for GET /customer-login
    @Test
    public void testShowCustomerLoginForm() throws Exception {
        mockMvc.perform(get("/customer-login"))
                .andExpect(status().isOk()) // Ensure the response is 200 OK
                .andExpect(view().name("customerLogin")); // Ensure the correct view is returned
    }

    //Tests for POST /customer-login
    @Test
    public void testLoginCustomer_Success() throws Exception {
        String email = "test@example.com";
        String password = "password123";
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setEmail(email);
        customer.setPassword(password);

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        mockMvc.perform(post("/customer-login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk()) // Ensure 200 OK status
                .andExpect(content().string("1")); // Expect customer ID as the response

        verify(customerRepository, times(1)).findByEmail(email);
    }
    @Test
    public void testLoginCustomer_InvalidPassword() throws Exception {
        String email = "test@example.com";
        String password = "wrongPassword";
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setEmail(email);
        customer.setPassword("password123");

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        mockMvc.perform(post("/customer-login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isUnauthorized()) // Ensure 401 Unauthorized status
                .andExpect(content().string("Invalid password")); // Expect error message in response

        verify(customerRepository, times(1)).findByEmail(email);
    }
    @Test
    public void testLoginCustomer_EmailNotFound() throws Exception {
        String email = "nonexistent@example.com";
        String password = "password123";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(post("/customer-login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isNotFound()) // Ensure 404 Not Found status
                .andExpect(content().string("Email not found")); // Expect error message in response

        // Verify repository interactions
        verify(customerRepository, times(1)).findByEmail(email);
    }


    // Tests for GET /shopper

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
                .andExpect(view().name("searchShops"));

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
                .andExpect(view().name("searchShops"));

        verify(shopRepository, times(1)).findAll();
    }

//    @Test
//    public void testOpenCustomerScreen_DatabaseError() throws Exception {
//        // Simulate a database error
//        when(shopRepository.findAll()).thenThrow(new RuntimeException("Database Error"));
//
//        mockMvc.perform(get("/shopper"))
//                .andExpect(status().is5xxServerError());
//
//        verify(shopRepository, times(1)).findAll();
//    }
}

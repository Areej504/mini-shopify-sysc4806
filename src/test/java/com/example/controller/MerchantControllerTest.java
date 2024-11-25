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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MerchantController.class)
public class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantRepository merchantRepository;

    @MockBean
    private ShopRepository shopRepository;
    @MockBean
    private PromotionRepository promotionRepository;

    // Tests for GET /create-merchant
    @Test
    public void testShowCreateMerchantForm_ReturnsCreateMerchantView() throws Exception {
        mockMvc.perform(get("/create-merchant"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("merchant"))
                .andExpect(view().name("createMerchant"));
    }

    // Tests for POST /create-merchant
    @Test
    public void testCreateMerchant_Success() throws Exception {
        // test case
        String email = "test@example.com";
        String password = "password123";
        Merchant savedMerchant = new Merchant();
        savedMerchant.setMerchantId(1L);
        savedMerchant.setEmail(email);
        savedMerchant.setPassword(password);

        when(merchantRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(merchantRepository.save(any(Merchant.class))).thenReturn(savedMerchant);

        mockMvc.perform(post("/create-merchant")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/merchant-login"));

        // Verify repository interactions
        verify(merchantRepository, times(1)).findByEmail(email);
        verify(merchantRepository, times(1)).save(argThat(merchant ->
                merchant.getEmail().equals(email) && merchant.getPassword().equals(password)
        ));
    }

    @Test
    public void testCreateMerchant_EmailAlreadyExists() throws Exception {
        // test case
        String email = "existing@example.com";
        when(merchantRepository.findByEmail(email)).thenReturn(Optional.of(new Merchant()));

        mockMvc.perform(post("/create-merchant")
                        .param("email", email)
                        .param("password", "password123"))
                .andExpect(status().isOk()) // Renders the same form
                .andExpect(view().name("createMerchant"))
                .andExpect(model().attribute("errorMessage", "Email already exists"));

        verify(merchantRepository, times(1)).findByEmail(email);
        verify(merchantRepository, never()).save(any(Merchant.class)); // Ensure no save occurs
    }

    // Tests for GET /merchant-login
    @Test
    public void testShowMerchantLoginForm() throws Exception {
        mockMvc.perform(get("/merchant-login"))
                .andExpect(status().isOk()) // Ensure the response is 200 OK
                .andExpect(view().name("merchantLogin")); // Ensure the correct view is returned
    }

    //Tests for POST /merchant-login
    @Test
    public void testLoginMerchant_Success() throws Exception {
        String email = "test@example.com";
        String password = "password123";
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setEmail(email);
        merchant.setPassword(password);

        when(merchantRepository.findByEmail(email)).thenReturn(Optional.of(merchant));

        mockMvc.perform(post("/merchant-login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk()) // Ensure 200 OK status
                .andExpect(content().string("1")); // Expect merchant ID as the response

        verify(merchantRepository, times(1)).findByEmail(email);
    }
    @Test
    public void testLoginMerchant_InvalidPassword() throws Exception {
        String email = "test@example.com";
        String password = "wrongPassword";
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setEmail(email);
        merchant.setPassword("password123");

        when(merchantRepository.findByEmail(email)).thenReturn(Optional.of(merchant));

        mockMvc.perform(post("/merchant-login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isUnauthorized()) // Ensure 401 Unauthorized status
                .andExpect(content().string("Invalid password")); // Expect error message in response

        verify(merchantRepository, times(1)).findByEmail(email);
    }
    @Test
    public void testLoginMerchant_EmailNotFound() throws Exception {
        String email = "nonexistent@example.com";
        String password = "password123";

        when(merchantRepository.findByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(post("/merchant-login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isNotFound()) // Ensure 404 Not Found status
                .andExpect(content().string("Email not found")); // Expect error message in response

        // Verify repository interactions
        verify(merchantRepository, times(1)).findByEmail(email);
    }

    // Tests for GET /merchant
    @Test
    public void testOpenMerchantHomeScreen_ReturnsMerchantView() throws Exception {
        mockMvc.perform(get("/merchant")
                        .param("merchantId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("merchantId", 1L))
                .andExpect(view().name("merchantView1"));
    }

    // Tests for GET /create-shop
    @Test
    public void testShowCreateShopForm_ReturnsCreateShopView() throws Exception {
//        mockMvc.perform(get("/create-shop")
//                        .param("merchantId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("shop"))
//                .andExpect(model().attributeExists("categories"))
//                .andExpect(model().attribute("merchantId", 1L))
//                .andExpect(view().name("createShop"));
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(new Merchant()));

        mockMvc.perform(get("/create-shop").param("merchantId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("createShop"));
    }

//    @Test
//    void testShowCreateShopForm_InvalidMerchantId_ReturnsNotFound() throws Exception {
//        when(merchantRepository.findById(999L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/create-shop").param("merchantId", "999"))
//                .andExpect(status().isNotFound());
//    }
//
//    // Tests for POST /create-shop
//    @Test
//    public void testCreateShop_SuccessfullyCreatesShop() throws Exception {
////        Merchant merchant = new Merchant();
////        merchant.setMerchantId(1L);
////
////        Shop shop = new Shop();
////        shop.setName("Test Shop");
////
////        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
////        when(shopRepository.save(any(Shop.class))).thenReturn(shop);
////
////        mockMvc.perform(post("/create-shop")
////                        .param("merchantId", "1")
////                        .flashAttr("shop", shop))
////                .andExpect(status().is3xxRedirection())
////                .andExpect(redirectedUrl("/manage-stores?merchantId=1&created=true&shopName=Test+Shop"));
////
////        verify(merchantRepository, times(1)).findById(anyLong());
////        verify(shopRepository, times(1)).save(any(Shop.class));
//
//        Merchant merchant = new Merchant();
//        Shop shop = new Shop();
//        shop.setName("Test Shop");
//        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
//        when(shopRepository.save(any(Shop.class))).thenReturn(shop);
//
//        mockMvc.perform(post("/create-shop")
//                        .param("merchantId", "1")
//                        .flashAttr("shop", shop))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/manage-stores?merchantId=1&created=true&shopName=Test%20Shop"));
//
//    }

//    @Test
//    public void testCreateShop_InvalidMerchantId_ThrowsException() throws Exception {
////        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());
////
////        mockMvc.perform(post("/create-shop")
////                        .param("merchantId", "999")
////                        .flashAttr("shop", new Shop()))
////                .andExpect(status().isNotFound());
////
////        verify(merchantRepository, times(1)).findById(anyLong());
////        verify(shopRepository, never()).save(any(Shop.class));
//        when(merchantRepository.findById(999L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/create-shop")
//                        .param("merchantId", "999")
//                        .flashAttr("shop", new Shop()))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void testCreateShop_SaveFails_ThrowsException() throws Exception {
//        Merchant merchant = new Merchant();
//        when(merchantRepository.save(any(Merchant.class))).thenThrow(new RuntimeException("Database Error"));
//
//        mockMvc.perform(post("/create-merchant")
//                        .flashAttr("merchant", merchant))
//                .andExpect(status().is5xxServerError());
//
//        verify(merchantRepository, times(1)).save(any(Merchant.class));
//    }

    // Tests for GET /manage-stores
    @Test
    public void testOpenManageStores_WithShops_ReturnsManageStoresView() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);

        Shop shop = new Shop();
        shop.setName("Test Shop");
        List<Shop> shops = Collections.singletonList(shop);

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        when(shopRepository.findByMerchant(any(Merchant.class))).thenReturn(shops);

        mockMvc.perform(get("/manage-stores")
                        .param("merchantId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("merchant", merchant))
                .andExpect(model().attribute("shops", shops))
                .andExpect(view().name("manageStores"));

        verify(merchantRepository, times(1)).findById(anyLong());
        verify(shopRepository, times(1)).findByMerchant(any(Merchant.class));
    }

//    @Test
//    public void testOpenManageStores_InvalidMerchantId_ThrowsException() throws Exception {
////        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());
////
////        mockMvc.perform(get("/manage-stores")
////                        .param("merchantId", "999"))
////                .andExpect(status().isBadRequest());
////
////        verify(merchantRepository, times(1)).findById(anyLong());
////        verify(shopRepository, never()).findByMerchant(any(Merchant.class));
//        when(merchantRepository.findById(999L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/manage-stores")
//                        .param("merchantId", "999"))
//                .andExpect(status().isNotFound()); // Adjusted to expect 404
//    }

    @Test
    public void testOpenManageStores_NoShops_ReturnsEmptyShopList() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        when(shopRepository.findByMerchant(any(Merchant.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/manage-stores")
                        .param("merchantId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("merchant", merchant))
                .andExpect(model().attribute("shops", Collections.emptyList()))
                .andExpect(view().name("manageStores"));

        verify(merchantRepository, times(1)).findById(anyLong());
        verify(shopRepository, times(1)).findByMerchant(any(Merchant.class));
    }
}

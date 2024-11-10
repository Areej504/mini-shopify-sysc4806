package com.example.controller;

import com.example.model.Merchant;
import com.example.model.MerchantRepository;
import com.example.model.Shop;
import com.example.model.ShopRepository;
import com.example.model.Category;
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
    public void testCreateMerchant_RedirectsToMerchantDashboard() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);

        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        mockMvc.perform(post("/create-merchant")
                        .flashAttr("merchant", merchant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/merchant?merchantId=1"));

        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    public void testCreateMerchant_SaveFails_ThrowsException() throws Exception {
        Merchant merchant = new Merchant();

        // Simulate save failure
        when(merchantRepository.save(any(Merchant.class))).thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(post("/create-merchant")
                        .flashAttr("merchant", merchant))
                .andExpect(status().isInternalServerError());

        verify(merchantRepository, times(1)).save(any(Merchant.class));
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
        mockMvc.perform(get("/create-shop")
                        .param("merchantId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shop"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("merchantId", 1L))
                .andExpect(view().name("createShop"));
    }

    // Tests for POST /create-shop
    @Test
    public void testCreateShop_SuccessfullyCreatesShop() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);

        Shop shop = new Shop();
        shop.setName("Test Shop");

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);

        mockMvc.perform(post("/create-shop")
                        .param("merchantId", "1")
                        .flashAttr("shop", shop))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manage-stores?merchantId=1&created=true&shopName=Test+Shop"));

        verify(merchantRepository, times(1)).findById(anyLong());
        verify(shopRepository, times(1)).save(any(Shop.class));
    }

    @Test
    public void testCreateShop_InvalidMerchantId_ThrowsException() throws Exception {
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/create-shop")
                        .param("merchantId", "999")
                        .flashAttr("shop", new Shop()))
                .andExpect(status().isBadRequest());

        verify(merchantRepository, times(1)).findById(anyLong());
        verify(shopRepository, never()).save(any(Shop.class));
    }

    @Test
    public void testCreateShop_SaveFails_ThrowsException() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        when(shopRepository.save(any(Shop.class))).thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(post("/create-shop")
                        .param("merchantId", "1")
                        .flashAttr("shop", new Shop()))
                .andExpect(status().isInternalServerError());

        verify(merchantRepository, times(1)).findById(anyLong());
        verify(shopRepository, times(1)).save(any(Shop.class));
    }

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

    @Test
    public void testOpenManageStores_InvalidMerchantId_ThrowsException() throws Exception {
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/manage-stores")
                        .param("merchantId", "999"))
                .andExpect(status().isBadRequest());

        verify(merchantRepository, times(1)).findById(anyLong());
        verify(shopRepository, never()).findByMerchant(any(Merchant.class));
    }

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

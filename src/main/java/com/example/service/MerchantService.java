package com.example.service;

import com.example.model.Shop;

import java.util.List;

public interface MerchantService {

    /**
     * Service to find a merchant IDs.
     *
     * @return The merchant's ID.
     */
    public Long getMerchantId();

    /**
     * Service to set a merchant's ID.
     *
     * @param id The provided ID to set for the merchant.
     */
    public void setMerchantId(Long id);

    /**
     * Service to get a merchant's name.
     *
     * @return The merchant's name.
     */
    public String getName();

    /**
     * Service to set a merchant's name.
     *
     * @param name The name of the merchant to set.
     */
    public void setName(String name);

    /**
     * Service to get a merchant's email.
     *
     * @return The merchant's email.
     */
    public String getEmail();

    /**
     * Service to set a merchant's email in the database.
     *
     * @param email The merchant's new email to update.
     */
    public void setEmail(String email);

    /**
     * Service to get a merchant's user password.
     *
     * @return The merchant's user password.
     */
    public String getPassword();

    /**
     * Service to set a merchant's user password.
     *
     * @param password The provided merchant's password.
     */
    public void setPassword(String password);

    /**
     * Service to get a list of shops that belong to a merchant.
     *
     * @return The merchant's list of shops.
     */
    public List<Shop> getShops();

    /**
     * Service to set a merchant's shops.
     *
     * @param shops The list of shops to allocate to the merchant.
     */
    public void setShops(List<Shop> shops);
}

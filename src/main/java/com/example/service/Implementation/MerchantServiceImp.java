package com.example.service.Implementation;

import com.example.repository.MerchantRepository;
import com.example.model.Shop;
import com.example.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantServiceImp implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    /**
     * Service to find a merchant IDs.
     *
     * @return The merchant's ID.
     */
    @Override
    public Long getMerchantId() {
        return 0L;
    }

    /**
     * Service to set a merchant's ID.
     *
     * @param id The provided ID to set for the merchant.
     */
    @Override
    public void setMerchantId(Long id) {

    }

    /**
     * Service to get a merchant's name.
     *
     * @return The merchant's name.
     */
    @Override
    public String getName() {
        return "";
    }

    /**
     * Service to set a merchant's name.
     *
     * @param name The name of the merchant to set.
     */
    @Override
    public void setName(String name) {

    }

    /**
     * Service to get a merchant's email.
     *
     * @return The merchant's email.
     */
    @Override
    public String getEmail() {
        return "";
    }

    /**
     * Service to set a merchant's email in the database.
     *
     * @param email The merchant's new email to update.
     */
    @Override
    public void setEmail(String email) {

    }

    /**
     * Service to get a merchant's user password.
     *
     * @return The merchant's user password.
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * Service to set a merchant's user password.
     *
     * @param password The provided merchant's password.
     */
    @Override
    public void setPassword(String password) {

    }

    /**
     * Service to get a list of shops that belong to a merchant.
     *
     * @return The merchant's list of shops.
     */
    @Override
    public List<Shop> getShops() {
        return List.of();
    }

    /**
     * Service to set a merchant's shops.
     *
     * @param shops The list of shops to allocate to the merchant.
     */
    @Override
    public void setShops(List<Shop> shops) {

    }
}

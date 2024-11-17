package com.example.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Merchant {

    @Id
    @GeneratedValue
    private Long merchantId;
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    List<Shop> shops;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long id){
        this.merchantId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops){
        this.shops = shops;
    }

    //Methods
    private void createShop(){
    }
    private void uploadProduct(){
    }
    private void updateInventory(){
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "merchantId=" + merchantId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", shops=" + (shops != null ? shops.size() : 0) + " shops" +
                '}';
    }

}

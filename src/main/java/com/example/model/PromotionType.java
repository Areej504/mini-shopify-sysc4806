package com.example.model;

public enum PromotionType {
    DISCOUNT_10_PERCENT("10% Off"),
    DISCOUNT_20_PERCENT("20% Off"),
    DISCOUNT_5_DOLLARS("Save $5"),
    BUY_ONE_GET_ONE("Buy One Get One Free"),
    CLEARANCE("Clearance"),
    FREE_SHIPPING("Free Shipping"),
    LOYALTY_POINTS("Earn Loyalty Points"),
    SEASONAL_HOLIDAY("Holiday Sale - Up to 25% Off"),
    NONE("No Promotion");

    private final String description;

    PromotionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

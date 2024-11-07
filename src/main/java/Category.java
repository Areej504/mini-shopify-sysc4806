public enum Category {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    ACCESSORIES("Accessories"),
    BOOKS("Books"),
    BEAUTY("Beauty"),
    HOME("Home"),
    TOYS("Toys"),
    SPORTS("Sports"),
    FOOD("Food");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

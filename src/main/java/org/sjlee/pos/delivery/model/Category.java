package org.sjlee.pos.delivery.model;

/**
 * Category Enum Represents Categories of Items
 */
public enum Category {
    APPETIZER("Appet & Salad"),
    ENTREE("Entree"),
    TEMPURA("Tempura"),
    UDON("Udon&Don"),
    NIGIRI("Nigiri Sushi"),
    OSHI("Oshi Sushi"),
    SASHIMI("Sashimi"),
    ROLL("Roll & Cone"),
    COMBO("Combo"),
    SPECIAL("Special Rolls");

    /**
     * Description is Used to Represent Category in UI
     **/
    private String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
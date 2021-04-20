package com.ng.emts.christmasOffer.util;

public enum Category {
    CATEGORY_A("A"),
    CATEGORY_B("B"),
    CATEGORY_C("C"),
    CATEGORY_D("D");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}

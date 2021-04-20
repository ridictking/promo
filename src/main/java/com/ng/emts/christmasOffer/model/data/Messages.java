package com.ng.emts.christmasOffer.model.data;


import com.ng.emts.christmasOffer.util.Category;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Messages {
    @Id
    private Long id;
    private String message;
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

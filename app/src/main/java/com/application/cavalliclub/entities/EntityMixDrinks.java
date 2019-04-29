package com.application.cavalliclub.entities;

public class EntityMixDrinks {

    String iv_drinks;
    String title_drinks;
    String description_drinks;
    String amount_drinks;

    public EntityMixDrinks(String iv_drinks, String title_drinks, String description_drinks, String amount_drinks) {
        this.iv_drinks = iv_drinks;
        this.title_drinks = title_drinks;
        this.description_drinks = description_drinks;
        this.amount_drinks = amount_drinks;
    }

    public String getIv_drinks() {
        return iv_drinks;
    }

    public void setIv_drinks(String iv_drinks) {
        this.iv_drinks = iv_drinks;
    }

    public String getTitle_drinks() {
        return title_drinks;
    }

    public void setTitle_drinks(String title_drinks) {
        this.title_drinks = title_drinks;
    }

    public String getDescription_drinks() {
        return description_drinks;
    }

    public void setDescription_drinks(String description_drinks) {
        this.description_drinks = description_drinks;
    }

    public String getAmount_drinks() {
        return amount_drinks;
    }

    public void setAmount_drinks(String amount_drinks) {
        this.amount_drinks = amount_drinks;
    }
}

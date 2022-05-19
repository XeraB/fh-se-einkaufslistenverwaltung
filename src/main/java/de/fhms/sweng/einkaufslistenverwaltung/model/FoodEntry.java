package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.time.LocalDate;

public class FoodEntry {
    private Integer id;

    private Integer amount;
    private LocalDate addedDate;
    private Boolean opened;

    public FoodEntry() {
    }

    public FoodEntry(FoodEntry fridge) {
        this.id = fridge.getId();
        this.amount = fridge.getAmount();
        this.addedDate = fridge.getAddedDate();
        this.opened = fridge.getOpened();
    }

    public Integer getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

}

package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.time.LocalDate;

public class FoodEntry {
    private Integer id;

    private Integer amount;
    private LocalDate addedDate;
    private Boolean opened;

    public FoodEntry() {
    }

    public FoodEntry(Integer id, Integer amount, LocalDate addedDate, Boolean opened) {
        this.id = id;
        this.amount = amount;
        this.addedDate = addedDate;
        this.opened = opened;
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

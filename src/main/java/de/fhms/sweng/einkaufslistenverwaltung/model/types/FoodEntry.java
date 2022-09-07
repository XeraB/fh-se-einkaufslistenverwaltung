package de.fhms.sweng.einkaufslistenverwaltung.model.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/* Wird bei POST-Anfrage an den FoodClient zurückgegeben */
@Getter
@Setter
@NoArgsConstructor
public class FoodEntry {
    private Integer id;

    private Integer amount;
    private LocalDate addedDate;
    private Boolean opened;

    public FoodEntry(Integer id, Integer amount, LocalDate addedDate, Boolean opened) {
        this.id = id;
        this.amount = amount;
        this.addedDate = addedDate;
        this.opened = opened;
    }
}
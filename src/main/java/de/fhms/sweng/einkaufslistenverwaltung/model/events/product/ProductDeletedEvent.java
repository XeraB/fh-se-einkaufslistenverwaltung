package de.fhms.sweng.einkaufslistenverwaltung.model.events.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductDeletedEvent extends ProductEvent {

    private String name;

    public ProductDeletedEvent(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
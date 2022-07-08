package de.fhms.sweng.einkaufslistenverwaltung.model.event;

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
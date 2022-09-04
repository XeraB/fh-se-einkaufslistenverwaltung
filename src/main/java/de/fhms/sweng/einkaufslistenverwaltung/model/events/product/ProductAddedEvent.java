package de.fhms.sweng.einkaufslistenverwaltung.model.events.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductAddedEvent extends ProductEvent {
    private String name;
    private Integer bestBeforeTime;
    private Integer price;

    public ProductAddedEvent(Integer id, String name, Integer bestBeforeTime, Integer price) {
        super(id);
        this.name = name;
        this.bestBeforeTime = bestBeforeTime;
        this.price = price;
    }
}
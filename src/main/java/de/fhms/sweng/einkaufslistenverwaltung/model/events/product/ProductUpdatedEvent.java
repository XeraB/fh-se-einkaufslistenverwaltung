package de.fhms.sweng.einkaufslistenverwaltung.model.events.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductUpdatedEvent extends ProductEvent {

    private String name;
    private Integer bestBeforeTime;
    private Integer price;

    public ProductUpdatedEvent(Integer id, String name, Integer bestBeforeTime, Integer price) {
        super(id);
        this.name = name;
        this.bestBeforeTime = bestBeforeTime;
        this.price = price;
    }

}

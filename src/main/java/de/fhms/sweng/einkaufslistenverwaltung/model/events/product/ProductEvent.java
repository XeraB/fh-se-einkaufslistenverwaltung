package de.fhms.sweng.einkaufslistenverwaltung.model.events.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductAddedEvent.class, name = "productAdded"),
        @JsonSubTypes.Type(value = ProductUpdatedEvent.class, name = "productUpdated"),
        @JsonSubTypes.Type(value = ProductDeletedEvent.class, name = "productDeleted")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class ProductEvent {

    private Integer id;

}
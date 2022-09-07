package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductEvent;

public interface EventPublisher {
    boolean publishEvent(ProductEvent event);
}

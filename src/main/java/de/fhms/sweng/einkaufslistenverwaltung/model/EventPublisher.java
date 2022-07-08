package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductEvent;

public interface EventPublisher {
    public boolean publishEvent(ProductEvent event);
}

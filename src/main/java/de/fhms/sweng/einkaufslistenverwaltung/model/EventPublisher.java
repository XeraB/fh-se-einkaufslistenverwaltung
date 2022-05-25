package de.fhms.sweng.einkaufslistenverwaltung.model;

public interface EventPublisher {
    public boolean publishEvent(ProductEvent event);
}

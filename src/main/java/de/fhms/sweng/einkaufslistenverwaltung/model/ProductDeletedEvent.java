package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.Objects;

public class ProductDeletedEvent extends ProductEvent{

    private String name;

    public ProductDeletedEvent() {
    }

    public ProductDeletedEvent(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return "ProductAddedEvent{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDeletedEvent)) return false;
        if (!super.equals(o)) return false;
        ProductDeletedEvent that = (ProductDeletedEvent) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}

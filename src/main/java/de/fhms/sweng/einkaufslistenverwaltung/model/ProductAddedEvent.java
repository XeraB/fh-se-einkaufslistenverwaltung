package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.Objects;

public class ProductAddedEvent extends ProductEvent {
    private String name;
    private Integer bestBeforeTime;
    private Integer price;

    public ProductAddedEvent() {
    }

    public ProductAddedEvent(Integer id, String name, Integer bestBeforeTime, Integer price) {
        super(id);
        this.name = name;
        this.bestBeforeTime = bestBeforeTime;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBestBeforeTime() {
        return bestBeforeTime;
    }

    public void setBestBeforeTime(Integer bestBeforeTime) {
        this.bestBeforeTime = bestBeforeTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductAddedEvent{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                ", bestBeforeTime=" + bestBeforeTime +
                ", price=" + price +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductAddedEvent)) return false;
        if (!super.equals(o)) return false;
        ProductAddedEvent that = (ProductAddedEvent) o;
        return name.equals(that.name) && bestBeforeTime.equals(that.bestBeforeTime) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, bestBeforeTime, price);
    }
}

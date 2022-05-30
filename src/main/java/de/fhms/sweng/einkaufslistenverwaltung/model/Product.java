package de.fhms.sweng.einkaufslistenverwaltung.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer bestBeforeTime;
    private Integer price;

    @Version
    private long version;

    @OneToMany(mappedBy = "shoppingList")
    private Set<ShoppingListProduct> shoppingListProducts;

    public Product() {
    }

    public Product(String name, Integer bestBeforeTime, Integer price) {
        this.name = name;
        this.bestBeforeTime = bestBeforeTime;
        this.price = price;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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
}

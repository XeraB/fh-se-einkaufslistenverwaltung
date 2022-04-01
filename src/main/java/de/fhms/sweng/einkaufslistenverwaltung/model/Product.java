package de.fhms.sweng.einkaufslistenverwaltung.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer bestBeforeTime;
    private Integer price;

    @OneToMany(mappedBy = "shoppingList")
    private List<ShoppingListProduct> shoppingListProducts;


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

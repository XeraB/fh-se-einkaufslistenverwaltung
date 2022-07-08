package de.fhms.sweng.einkaufslistenverwaltung.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Product(String name, Integer bestBeforeTime, Integer price) {
        this.name = name;
        this.bestBeforeTime = bestBeforeTime;
        this.price = price;
    }
}

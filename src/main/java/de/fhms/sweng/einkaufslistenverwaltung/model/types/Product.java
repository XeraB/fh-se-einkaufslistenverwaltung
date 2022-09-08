package de.fhms.sweng.einkaufslistenverwaltung.model.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
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

    @JsonIgnore
    @OneToMany(mappedBy = "shoppingList")
    private List<ShoppingListProduct> shoppingListProducts;

    public Product(String name, Integer bestBeforeTime, Integer price) {
        this.name = name;
        this.bestBeforeTime = bestBeforeTime;
        this.price = price;
    }
}

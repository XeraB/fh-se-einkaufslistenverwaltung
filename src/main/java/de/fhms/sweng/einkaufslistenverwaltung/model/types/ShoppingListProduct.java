package de.fhms.sweng.einkaufslistenverwaltung.model.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shopping_list_product")
@IdClass(ShoppingListProductId.class)
public class ShoppingListProduct {

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "shoppingList_id", referencedColumnName = "id")
    private ShoppingList shoppingList;

    private int amount;

    private Unit unit;

    @Version
    private long version;

    public ShoppingListProduct(Product product, ShoppingList shoppingList, int amount) {
        this.product = product;
        this.shoppingList = shoppingList;
        this.amount = amount;
    }
}

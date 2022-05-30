package de.fhms.sweng.einkaufslistenverwaltung.model;

import javax.persistence.*;

@Entity
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

    @Version
    private long version;

    public ShoppingListProduct() {
    }

    public ShoppingListProduct(Product product, ShoppingList shoppingList, int amount) {
        this.product = product;
        this.shoppingList = shoppingList;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

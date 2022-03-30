package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.io.Serializable;

public class ShoppingListProductId implements Serializable {

    private int product;
    private int shoppingList;

    public int hashCode() {
        return (int)(product + shoppingList);
    }

    public boolean equals(Object object) {
        if (object instanceof ShoppingListProductId) {
            ShoppingListProductId otherId = (ShoppingListProductId) object;
            return (otherId.product == this.product)
                    && (otherId.shoppingList == this.shoppingList);
        }
        return false;
    }
}

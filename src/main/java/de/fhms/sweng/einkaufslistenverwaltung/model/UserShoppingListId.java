package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.io.Serializable;

public class UserShoppingListId implements Serializable {

    private int user;
    private int shoppingList;

    public int hashCode() {
        return (int)(user + shoppingList);
    }

    public boolean equals(Object object) {
        if (object instanceof UserShoppingListId) {
            UserShoppingListId otherId = (UserShoppingListId) object;
            return (otherId.user == this.user)
                    && (otherId.shoppingList == this.shoppingList);
        }
        return false;
    }
}

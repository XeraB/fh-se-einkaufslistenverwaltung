package de.fhms.sweng.einkaufslistenverwaltung.model;

import javax.persistence.*;

@Entity
@Table(name = "user_shopping_list")
@IdClass(UserShoppingListId.class)
public class UserShoppingList {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "shoppingList_id", referencedColumnName = "id")
    private ShoppingList shoppingList;

    public UserShoppingList(){}

    public UserShoppingList(User user, ShoppingList shoppingList) {
        this.user = user;
        this.shoppingList = shoppingList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }
}

package de.fhms.sweng.einkaufslistenverwaltung.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserShoppingList> userShoppingLists;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ShoppingListProduct> shoppingListProducts;

    private String name;

    public ShoppingList() {
    }

    public ShoppingList(String name) {
        this.name = name;
    }

    public ShoppingList(Integer id, List<UserShoppingList> userShoppingLists, List<ShoppingListProduct> shoppingListProducts, String name) {
        this.id = id;
        this.userShoppingLists = userShoppingLists;
        this.shoppingListProducts = shoppingListProducts;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<UserShoppingList> getUserShoppingLists() {
        return userShoppingLists;
    }

    public void setUserShoppingLists(List<UserShoppingList> userShoppingLists) {
        this.userShoppingLists = userShoppingLists;
    }

    public List<ShoppingListProduct> getShoppingListProducts() {
        return shoppingListProducts;
    }

    public void setShoppingListProducts(List<ShoppingListProduct> shoppingListProducts) {
        this.shoppingListProducts = shoppingListProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ShoppingListProduct getProductFromShoppingListById(Integer num) {
        return null;
    }

    public ShoppingListProduct getEntry(Integer id, Integer num) {
        return null;
    }
}

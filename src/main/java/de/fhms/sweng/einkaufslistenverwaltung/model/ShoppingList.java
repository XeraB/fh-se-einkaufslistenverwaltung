package de.fhms.sweng.einkaufslistenverwaltung.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "shoppingList_FK")
    private Set<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<ShoppingListProduct> shoppingListProducts;

    private String name;

    public ShoppingList() {
    }

    public ShoppingList(String name) {
        this.name = name;
    }

    public ShoppingList(Integer id, Set<ShoppingListProduct> shoppingListProducts, String name) {
        this.id = id;
        this.shoppingListProducts = shoppingListProducts;
        this.name = name;
    }

    public ShoppingList(User firstUser, String name) {
        this.shoppingListProducts = new HashSet<ShoppingListProduct>();
        this.users = new HashSet<User>();
        this.users.add(firstUser);
    }

    public Integer getUserCount() {
        return users.size();
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<ShoppingListProduct> getShoppingListProducts() {
        return shoppingListProducts;
    }

    public void setShoppingListProducts(Set<ShoppingListProduct> shoppingListProducts) {
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

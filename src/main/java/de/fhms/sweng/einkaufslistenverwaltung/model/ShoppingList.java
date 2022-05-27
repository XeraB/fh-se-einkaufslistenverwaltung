package de.fhms.sweng.einkaufslistenverwaltung.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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

    private String inviteCode;

    public ShoppingList() {
    }

    public ShoppingList(Integer id, Set<ShoppingListProduct> shoppingListProducts) {
        this.id = id;
        this.shoppingListProducts = shoppingListProducts;
        this.users = new HashSet<User>();
    }

    public ShoppingList(User firstUser) {
        this.shoppingListProducts = new HashSet<ShoppingListProduct>();
        this.users = new HashSet<User>();
        this.addUser(firstUser);
    }

    public Integer getUserCount() {
        return users.size();
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<ShoppingListProduct> getShoppingListProducts() {
        return shoppingListProducts;
    }

    public void setShoppingListProducts(Set<ShoppingListProduct> shoppingListProducts) {
        this.shoppingListProducts = shoppingListProducts;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "id=" + id +
                ", users=" + users +
                ", shoppingListProducts=" + shoppingListProducts +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingList that = (ShoppingList) o;
        return id.equals(that.id) && Objects.equals(users, that.users) && Objects.equals(shoppingListProducts, that.shoppingListProducts) && Objects.equals(inviteCode, that.inviteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, shoppingListProducts, inviteCode);
    }
}

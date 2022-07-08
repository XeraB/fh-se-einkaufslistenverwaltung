package de.fhms.sweng.einkaufslistenverwaltung.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @Version
    private long version;

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

}

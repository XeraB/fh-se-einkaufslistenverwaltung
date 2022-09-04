package de.fhms.sweng.einkaufslistenverwaltung.model.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;

    @Column(name = "shoppingList_FK")
    private Integer shoppingListId;

    private Role role;

    @Version
    private long version;

    public User(Integer id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}

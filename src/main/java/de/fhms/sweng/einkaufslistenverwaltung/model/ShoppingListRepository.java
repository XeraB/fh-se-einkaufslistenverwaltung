package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.Optional;
import java.util.Set;

public interface ShoppingListRepository {

    Optional<ShoppingList> findById(Integer id);

    Optional<ShoppingList> findByUsers_id(Integer id);

    ShoppingList save(ShoppingList s);

    void delete(ShoppingList shoppingList);

    Optional<ShoppingList> findByInviteCode(String inviteCode);

}

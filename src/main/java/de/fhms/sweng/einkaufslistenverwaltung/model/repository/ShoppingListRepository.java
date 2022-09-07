package de.fhms.sweng.einkaufslistenverwaltung.model.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.ShoppingList;

import java.util.Optional;

public interface ShoppingListRepository {

    Optional<ShoppingList> findById(Integer id);

    Optional<ShoppingList> findByUsers_id(Integer id);

    ShoppingList save(ShoppingList s);

    void delete(ShoppingList shoppingList);

    Optional<ShoppingList> findByInviteCode(String inviteCode);

}

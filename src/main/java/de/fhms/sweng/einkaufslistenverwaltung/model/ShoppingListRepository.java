package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository {

    Optional<ShoppingList> findById(Integer id);

    ShoppingList save(ShoppingList s);

    void delete(ShoppingList shoppingList);

    Optional<ShoppingList> findByName(String name);

    Optional<List<ShoppingList>> getAll();
}

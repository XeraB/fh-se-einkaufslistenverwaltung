package de.fhms.sweng.einkaufslistenverwaltung.model.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.ShoppingListProduct;

import java.util.Set;

public interface ShoppingListProductRepository {

    Iterable<ShoppingListProduct> findAll();

    Set<ShoppingListProduct> findAllByShoppingList_Id(Integer shoppingList_id);

    ShoppingListProduct save(ShoppingListProduct s);

    void delete(ShoppingListProduct shoppingListProduct);

    void deleteAllByShoppingList_Id(Integer shoppingList_id);
}

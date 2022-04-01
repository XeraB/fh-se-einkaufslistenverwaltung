package de.fhms.sweng.einkaufslistenverwaltung.model;

public interface ShoppingListProductRepository {

    Iterable<ShoppingListProduct> findAll();
    ShoppingListProduct save(ShoppingListProduct s);

    void delete(ShoppingListProduct shoppingListProduct);
}

package de.fhms.sweng.einkaufslistenverwaltung.outbound.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.ShoppingListProduct;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListProductRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ShoppingListProductJpaRepository extends CrudRepository<ShoppingListProduct, Integer>, ShoppingListProductRepository {
    Iterable<ShoppingListProduct> findAll();

    Set<ShoppingListProduct> findAllByShoppingList_Id(Integer shoppingList_id);

    void deleteAllByShoppingList_Id(Integer shoppingList_id);
}

package de.fhms.sweng.einkaufslistenverwaltung.outbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingList;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ShoppingListJpaRepository extends CrudRepository<ShoppingList, Integer>, ShoppingListRepository {

    @Query("SELECT l FROM ShoppingList l")
    Optional<List<ShoppingList>> getAll();

    /**
     @Query("SELECT ShoppingList.id FROM ShoppingList WHERE UserShoppingList.shoppingList.id = ShoppingList.id AND User.id = :userId")
     Optional<ShoppingList> findAll(Integer userId);
     */
}

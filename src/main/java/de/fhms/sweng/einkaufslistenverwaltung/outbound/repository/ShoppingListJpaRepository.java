package de.fhms.sweng.einkaufslistenverwaltung.outbound.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.ShoppingList;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShoppingListJpaRepository extends CrudRepository<ShoppingList, Integer>, ShoppingListRepository {

    Optional<ShoppingList> findByUsers_id(Integer id);

    ShoppingList save(ShoppingList shoppingList);
}

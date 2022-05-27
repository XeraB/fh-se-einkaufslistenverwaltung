package de.fhms.sweng.einkaufslistenverwaltung.outbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingList;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.Optional;

public interface ShoppingListJpaRepository extends CrudRepository<ShoppingList, Integer>, ShoppingListRepository {

    Optional<ShoppingList> findByUsers_id(Integer id);

    ShoppingList save(ShoppingList shoppingList);
}

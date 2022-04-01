package de.fhms.sweng.einkaufslistenverwaltung.outbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.UserShoppingList;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserShoppingListRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserShoppingListJpaRepository extends CrudRepository<UserShoppingList, Integer>, UserShoppingListRepository {

    Iterable<UserShoppingList> findAll();
    UserShoppingList save(UserShoppingList s);

}

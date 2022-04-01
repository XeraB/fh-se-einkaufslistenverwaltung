package de.fhms.sweng.einkaufslistenverwaltung.outbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListProduct;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListProductRepository;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingListProductJpaRepository extends CrudRepository<ShoppingListProduct, Integer>, ShoppingListProductRepository {
    Iterable<ShoppingListProduct> findAll();

}

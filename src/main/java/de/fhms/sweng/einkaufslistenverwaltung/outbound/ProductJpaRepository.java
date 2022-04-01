package de.fhms.sweng.einkaufslistenverwaltung.outbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.ProductRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends CrudRepository<Product, Integer>, ProductRepository {

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p")
    Optional<List<Product>> getAll();

}

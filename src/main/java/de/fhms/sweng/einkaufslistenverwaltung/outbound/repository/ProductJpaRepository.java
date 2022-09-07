package de.fhms.sweng.einkaufslistenverwaltung.outbound.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductJpaRepository extends CrudRepository<Product, Integer>, ProductRepository {

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p")
    Optional<Set<Product>> getAll();

}

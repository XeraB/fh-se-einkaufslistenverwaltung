package de.fhms.sweng.einkaufslistenverwaltung.model.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.Product;

import java.util.Optional;
import java.util.Set;

public interface ProductRepository {

    Optional<Product> findById(Integer id);

    Optional<Product> findByName(String name);

    Product save(Product p);

    void delete(Product p);

    Optional<Set<Product>> getAll();
}

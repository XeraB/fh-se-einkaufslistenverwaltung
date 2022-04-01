package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Integer id);

    Optional<Product> findByName(String name);

    Product save(Product p);

    void delete(Product p);

    Optional<List<Product>> getAll();
}

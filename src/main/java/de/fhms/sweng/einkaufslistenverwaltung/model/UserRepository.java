package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Integer id);

    void deleteById(Integer id);
}

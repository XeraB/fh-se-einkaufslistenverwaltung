package de.fhms.sweng.einkaufslistenverwaltung.model.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Optional<User> findById(Integer id);

    void deleteById(Integer id);

    User findByEmail(String email);

    User save(User user);

    Set<User> findByName(String name);
}

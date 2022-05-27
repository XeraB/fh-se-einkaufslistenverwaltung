package de.fhms.sweng.einkaufslistenverwaltung.model;

import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Optional<User> findById(Integer id);

    void deleteById(Integer id);

    User findByEmail(String email);

    Set<User> findByName(String name);
}

package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.List;

public interface UserRepository {

    User findByEmail(String email);

    List<User> findByName(String name);
}

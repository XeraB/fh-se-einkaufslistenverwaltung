package de.fhms.sweng.einkaufslistenverwaltung.outbound.repository;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.User;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserJpaRepository extends CrudRepository<User, Integer>, UserRepository {

    Set<User> findByName(String name);

    void deleteById(Integer id);

    User findByEmail(String email);
}

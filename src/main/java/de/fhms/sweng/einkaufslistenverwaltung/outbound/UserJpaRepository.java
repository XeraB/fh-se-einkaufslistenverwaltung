package de.fhms.sweng.einkaufslistenverwaltung.outbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.User;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserJpaRepository extends CrudRepository<User, Integer>, UserRepository {

    Set<User> findByName(String name);

    void deleteById(Integer id);

    User findByEmail(String email);
}

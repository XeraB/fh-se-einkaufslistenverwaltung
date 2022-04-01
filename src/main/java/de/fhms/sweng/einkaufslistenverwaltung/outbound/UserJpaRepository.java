package de.fhms.sweng.einkaufslistenverwaltung.outbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.User;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends CrudRepository<User, Integer>, UserRepository {

    List<User> findByName(String name);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

}

package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.model.repository.UserRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Role;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.User;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;

@Service
@Retryable(include = {OptimisticLockException.class, StaleStateException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100, maxDelay = 500))
public class UserService {

    private UserRepository userRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveRegisteredUser(Integer userId, String userEmail, String userName, Role role) {
        LOGGER.info("Execute saveRegisteredUser()");
        User user = new User(userId, userName, userEmail, role);
        userRepository.save(user);
    }

    @Transactional
    public void update(Integer userId, String userEmail, String userName, Role role) {
        LOGGER.info("Execute saveRegisteredUser()");
        User user = userRepository.findById(userId).get();
        user.setEmail(userEmail);
        user.setName(userName);
        user.setRole(role);
    }
}

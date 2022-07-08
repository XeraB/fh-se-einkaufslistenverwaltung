package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.UserDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.UserEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.UserRegisteredEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.function.Consumer;

@Component
public class UserEventConsumer implements Consumer<UserEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final ShoppingListService shoppingListService;

    private final UserRepository userRepository;

    @Autowired
    public UserEventConsumer(ShoppingListService shoppingListService, UserRepository userRepository) {
        this.shoppingListService = shoppingListService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void accept(UserEvent userEvent) {
        if (userEvent instanceof UserRegisteredEvent) {
            accept((UserRegisteredEvent) userEvent);
        }
        if (userEvent instanceof UserDeletedEvent) {
            accept((UserDeletedEvent) userEvent);
        }
    }

    public void accept(UserDeletedEvent userEvent) {
        LOGGER.info("Consumed Event: " + userEvent);
        this.shoppingListService.deleteShoppingList(userEvent.getEmail());
    }

    public void accept(UserRegisteredEvent userEvent) {
        LOGGER.info("Consumed Event: " + userEvent);
        this.shoppingListService.addUserWithNewShoppingList(
                userEvent.getUserId(),
                userEvent.getName(),
                userEvent.getEmail()
        );
    }


}

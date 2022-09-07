package de.fhms.sweng.einkaufslistenverwaltung.inbound.messaging;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserService;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.user.UserDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.user.UserEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.user.UserRegisteredEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.user.UserUpdatedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.User;
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

    private final UserService userService;

    @Autowired
    public UserEventConsumer(ShoppingListService shoppingListService, UserService userService) {
        this.shoppingListService = shoppingListService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void accept(UserEvent userEvent) {
        if (userEvent instanceof UserRegisteredEvent) {
            accept((UserRegisteredEvent) userEvent);
        } else if (userEvent instanceof UserUpdatedEvent) {
            accept((UserUpdatedEvent) userEvent);
        } else if (userEvent instanceof UserDeletedEvent) {
            accept((UserDeletedEvent) userEvent);
        }
    }

    public void accept(UserDeletedEvent userDeletedEvent) {
        LOGGER.info("Consumed Event: " + userDeletedEvent);
        this.shoppingListService.deleteShoppingList(userDeletedEvent.getEmail());
    }

    public void accept(UserRegisteredEvent userRegisteredEvent) {
        LOGGER.info("Consumed Event: " + userRegisteredEvent);
        this.userService.saveRegisteredUser(userRegisteredEvent.getUserId(), userRegisteredEvent.getEmail(), userRegisteredEvent.getName(), userRegisteredEvent.getRole());
    }

    public void accept(UserUpdatedEvent userUpdatedEvent) {
        LOGGER.info("Consumed Event: " + userUpdatedEvent);
        this.userService.update(userUpdatedEvent.getUserId(), userUpdatedEvent.getEmail(), userUpdatedEvent.getName(), userUpdatedEvent.getRole());
    }
}

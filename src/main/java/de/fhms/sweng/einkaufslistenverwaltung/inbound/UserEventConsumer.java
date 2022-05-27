package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UserEventConsumer implements Consumer<UserEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final ShoppingListService shoppingListService;

    @Autowired
    public UserEventConsumer(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @Override
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
        this.shoppingListService.deleteShoppingList(userEvent.getUserId());
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

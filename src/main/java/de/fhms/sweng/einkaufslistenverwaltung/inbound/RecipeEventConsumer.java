package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductAddedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class RecipeEventConsumer implements Consumer<ProductEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final ShoppingListService shoppingListService;

    @Autowired
    public RecipeEventConsumer(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @Override
    public void accept(ProductEvent productEvent) {
        if (productEvent instanceof ProductAddedEvent) {
            accept((ProductAddedEvent) productEvent);
        }
        if (productEvent instanceof ProductUpdatedEvent) {
            accept((ProductUpdatedEvent) productEvent);
        }
        if (productEvent instanceof ProductDeletedEvent) {
            accept((ProductDeletedEvent) productEvent);
        }
    }

    //TODO
    public void accept(ProductAddedEvent productAddedEvent) {
        LOGGER.info("Consumed Event: " + productAddedEvent);
    }

    public void accept(ProductUpdatedEvent productUpdatedEvent) {
        LOGGER.info("Consumed Event: " + productUpdatedEvent);
    }

    public void accept(ProductDeletedEvent productDeletedEvent) {
        LOGGER.info("Consumed Event: " + productDeletedEvent);
    }
}

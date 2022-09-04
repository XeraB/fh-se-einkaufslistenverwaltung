package de.fhms.sweng.einkaufslistenverwaltung.inbound.messaging;

import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductAddedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductUpdatedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class FoodEventConsumer implements Consumer<ProductEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final ProductService productService;

    @Autowired
    public FoodEventConsumer(ProductService productService) {
        this.productService = productService;
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

    public void accept(ProductAddedEvent productAddedEvent) {
        LOGGER.info("Consumed Event: " + productAddedEvent);
        productService.addProductFromEvent(productAddedEvent.getId(), productAddedEvent.getName(), productAddedEvent.getBestBeforeTime(), productAddedEvent.getPrice());
    }

    public void accept(ProductUpdatedEvent productUpdatedEvent) {
        LOGGER.info("Consumed Event: " + productUpdatedEvent);
        productService.updateProductFromEvent(productUpdatedEvent.getId(), productUpdatedEvent.getName(), productUpdatedEvent.getBestBeforeTime(), productUpdatedEvent.getPrice());
    }

    public void accept(ProductDeletedEvent productDeletedEvent) {
        LOGGER.info("Consumed Event: " + productDeletedEvent);
        productService.deleteProductFromEvent(productDeletedEvent.getId());
    }
}

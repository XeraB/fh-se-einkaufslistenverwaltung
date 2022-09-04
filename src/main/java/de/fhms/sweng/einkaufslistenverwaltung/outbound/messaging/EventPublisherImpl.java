package de.fhms.sweng.einkaufslistenverwaltung.outbound.messaging;

import de.fhms.sweng.einkaufslistenverwaltung.model.EventPublisher;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherImpl implements EventPublisher {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private StreamBridge streamBridge;

    @Autowired
    public EventPublisherImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public boolean publishEvent(ProductEvent event) {
        var successfullyPublished = streamBridge.send("eventPublisher-out-0", event);
        if (successfullyPublished)
            LOGGER.info("Event published: " + event);
        else
            LOGGER.error("Publishing event failed: " + event);
        return successfullyPublished;
    }
}

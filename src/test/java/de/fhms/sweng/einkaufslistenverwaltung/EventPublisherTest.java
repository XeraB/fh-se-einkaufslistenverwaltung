package de.fhms.sweng.einkaufslistenverwaltung;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhms.sweng.einkaufslistenverwaltung.model.EventPublisher;
import de.fhms.sweng.einkaufslistenverwaltung.model.ProductAddedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestChannelBinderConfiguration.class)
public class EventPublisherTest {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private EventPublisher eventPublisher;

    @MockBean
    private ShoppingListService spyShoppingListService;

    @Test
    void testPublishEvent() throws Exception {
        var event = new ProductAddedEvent(42, "Banane", 4, 4);
        this.eventPublisher.publishEvent(event);
        var receivedMessage = this.outputDestination.receive();
        assertNotNull(receivedMessage);
        assertTrue(receivedMessage.getPayload().length > 0);
        var payloadObject = (new ObjectMapper()).readValue(receivedMessage.getPayload(), ProductAddedEvent.class);
        assertEquals(event, payloadObject);
    }
}

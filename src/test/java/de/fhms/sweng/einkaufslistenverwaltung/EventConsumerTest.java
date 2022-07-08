package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.UserDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.UserRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestChannelBinderConfiguration.class)
public class EventConsumerTest {

    @Autowired
    private InputDestination inputDestination;

    @MockBean
    private ShoppingListService spyShoppingListService;

    @Test
    void testConsumeRegisterUserMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new UserRegisteredEvent(42, "Gustav", "gustav@web.de", ""))
                        .build()
        );
        //checks if the EventConsumer class triggers the right service method on consumption of an event:
        verify(this.spyShoppingListService, times(1)).addUserWithNewShoppingList(42, "Gustav", "gustav@web.de");
    }

    @Test
    void testConsumeDeleteUserMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new UserDeletedEvent(42, "Gustav", "gustav@web.de"))
                        .build()
        );
        //checks if the EventConsumer class triggers the right service method on consumption of an event:
        verify(this.spyShoppingListService, times(1)).deleteShoppingList("gustav@web.de");
    }

}

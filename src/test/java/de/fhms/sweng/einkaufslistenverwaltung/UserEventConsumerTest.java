package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import de.fhms.sweng.einkaufslistenverwaltung.model.UserService;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductAddedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductUpdatedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.user.UserDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.user.UserRegisteredEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.user.UserUpdatedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Role;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestChannelBinderConfiguration.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "spring.cloud.function.definition=userEventConsumer")
public class UserEventConsumerTest {

    @Autowired
    private InputDestination inputDestination;

    @MockBean
    private ShoppingListService spyShoppingListService;

    @MockBean
    private UserService userService;

    private User user;

    private static final Integer TEST_ID = 1;
    private static final String TEST_EMAIL = "testname@test";
    private static final String TEST_NAME = "test name";
    private static final Role TEST_Role = Role.USER;


    @BeforeEach
    void setUp() {
        user = new User(TEST_ID, TEST_EMAIL, TEST_NAME, TEST_Role);
    }

    @Test
    void testConsumeRegisterUserMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new UserRegisteredEvent(user.getId(), user.getName(), user.getEmail(), user.getRole()))
                        .build()
        );
        verify(this.userService, times(1)).saveRegisteredUser(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }

    @Test
    void testConsumeDeleteUserMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new UserDeletedEvent(42, "Gustav", "gustav@web.de"))
                        .build()
        );
        verify(this.spyShoppingListService, times(1)).deleteShoppingList("gustav@web.de");
    }

    @Test
    void testConsumeUpdateUserMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new UserUpdatedEvent(42, "Gustav", "gustav@web.de", Role.USER))
                        .build()
        );
        verify(this.userService, times(1)).update(42, "gustav@web.de", "Gustav", Role.USER);
    }
}

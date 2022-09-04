package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductAddedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductUpdatedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
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
@TestPropertySource(properties = "spring.cloud.function.definition=foodEventConsumer")
public class FoodEventConsumerTest {

    @Autowired
    private InputDestination inputDestination;

    @MockBean
    private ProductService productService;

    private Product product;

    private static final Integer TEST_PRODUCT_ID = 1;
    private static final String TEST_PRODUCT_NAME = "Apfel";
    private static final Integer TEST_PRODUCT_TIME = 5;
    private static final Integer TEST_PRODUCT_PRICE = 4;

    @BeforeEach
    void setUp() {
        product = new Product(TEST_PRODUCT_NAME, TEST_PRODUCT_TIME, TEST_PRODUCT_PRICE);
        product.setId(TEST_PRODUCT_ID);
    }

    @Test
    void testConsumeAddProductMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new ProductAddedEvent(product.getId(), product.getName(), product.getBestBeforeTime(), product.getPrice()))
                        .build()
        );
        verify(this.productService, times(1)).addProductFromEvent(product.getId(), product.getName(), product.getBestBeforeTime(), product.getPrice());
    }

    @Test
    void testConsumeUpdateProductMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new ProductUpdatedEvent(product.getId(), product.getName(), product.getBestBeforeTime(), product.getPrice()))
                        .build()
        );
        verify(this.productService, times(1)).updateProductFromEvent(product.getId(), product.getName(), product.getBestBeforeTime(), product.getPrice());
    }

    @Test
    void testConsumeDeleteProductMessage() {
        this.inputDestination.send(
                MessageBuilder
                        .withPayload(new ProductDeletedEvent(product.getId(), product.getName()))
                        .build()
        );
        verify(this.productService, times(1)).deleteProductFromEvent(product.getId());
    }
}

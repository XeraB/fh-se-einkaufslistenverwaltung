package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private EventPublisher eventPublisher;

    private ProductAddedEvent productAddedEvent;

    private ProductDeletedEvent productDeletedEvent;

    private ProductService productService;
    private Set<Product> products;
    private Product product;

    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "Apfel";
    private static final Integer TEST_TIME = 5;
    private static final Integer TEST_PRICE = 4;

    private static final Integer TEST_ID_FALSE = 2;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productRepository, eventPublisher);
        this.products = new HashSet<>();
        this.product = new Product(TEST_NAME, TEST_TIME, TEST_PRICE);
        this.product.setId(TEST_ID);
        productAddedEvent = new ProductAddedEvent(TEST_ID, TEST_NAME, TEST_TIME, TEST_PRICE);
        productDeletedEvent = new ProductDeletedEvent(TEST_ID, TEST_NAME);
    }

    @Test
    void getProductById() {
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(product));
        Product product = productService.getProductById(TEST_ID);
        assertThat(product.getId(), is(TEST_ID));
        assertThat(product.getName(), is(TEST_NAME));
        assertThat(product.getBestBeforeTime(), is(TEST_TIME));
        assertThat(product.getPrice(), is(TEST_PRICE));
    }

    @Test
    void getProductByIdException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(TEST_ID_FALSE);
        });
    }

    @Test
    void getAllProducts() {
        given(productRepository.getAll()).willReturn(Optional.of(products));
        products.add(product);

        Set<Product> productList = productService.getAllProducts();
        assertFalse(productList.isEmpty());
    }

    @Test
    void getAllProductsException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.getAllProducts();
        });
    }

    @Test
    void addProduct() {
        given(productRepository.getAll()).willReturn(Optional.of(products));
        given(eventPublisher.publishEvent(productAddedEvent)).willReturn(true);

        assertFalse(productService.getAllProducts().contains(product));
        Product product = productService.addProduct(this.product);
        assertThat(product.getName(), is(TEST_NAME));
        assertThat(product.getBestBeforeTime(), is(TEST_TIME));
        assertThat(product.getPrice(), is(TEST_PRICE));
    }

    @Test
    void addProductException() {
        given(productRepository.getAll()).willReturn(Optional.of(products));
        products.add(product);

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            productService.addProduct(product);
        });
    }

    @Test
    void deleteProduct() {
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(product));
        given(productRepository.getAll()).willReturn(Optional.of(products));
        given(eventPublisher.publishEvent(productDeletedEvent)).willReturn(true);
        products.add(product);

        assertTrue(productService.getAllProducts().contains(product));
        productService.deleteProduct(TEST_ID);
        Mockito.verify(productRepository, times(1)).delete(product);
    }
}
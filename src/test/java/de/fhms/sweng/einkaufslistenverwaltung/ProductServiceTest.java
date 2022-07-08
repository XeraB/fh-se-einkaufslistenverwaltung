package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.model.EventPublisher;
import de.fhms.sweng.einkaufslistenverwaltung.model.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductAddedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.event.ProductUpdatedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.exception.AlreadyExistException;
import de.fhms.sweng.einkaufslistenverwaltung.model.exception.ResourceNotFoundException;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private EventPublisher eventPublisher;

    private ProductAddedEvent productAddedEvent;
    private ProductUpdatedEvent productUpdatedEvent;

    private ProductDeletedEvent productDeletedEvent;

    private ProductService productService;
    private Set<Product> products;
    private Product product;

    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "Apfel";
    private static final Integer TEST_TIME = 5;
    private static final Integer TEST_PRICE = 4;

    private static final Integer TEST_ID_FALSE = 2;
    private static final String TEST_UPDATE_NAME = "Apfel 2";
    private static final Integer TEST_UPDATE_TIME = 6;
    private static final Integer TEST_UPDATE_PRICE = 3;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productRepository, eventPublisher);
        this.products = new HashSet<>();
        this.product = new Product(TEST_NAME, TEST_TIME, TEST_PRICE);
        this.product.setId(TEST_ID);
        productAddedEvent = new ProductAddedEvent(TEST_ID, TEST_NAME, TEST_TIME, TEST_PRICE);
        productUpdatedEvent = new ProductUpdatedEvent(TEST_ID, TEST_UPDATE_NAME, TEST_UPDATE_TIME, TEST_UPDATE_PRICE);
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
    void addProductException1() {
        given(productRepository.getAll()).willReturn(Optional.of(products));
        products.add(product);

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            productService.addProduct(product);
        });
    }

    @Test
    void addProductException2() {
        given(productRepository.getAll()).willReturn(Optional.of(products));

        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.addProduct(product);
        });
    }

    @Test
    void addProductException3() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.addProduct(product);
        });
    }

    @Test
    void updateProduct() {
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(this.product));
        given(eventPublisher.publishEvent(productUpdatedEvent)).willReturn(true);

        Product updateProduct = new Product(TEST_UPDATE_NAME, TEST_UPDATE_TIME, TEST_UPDATE_PRICE);
        updateProduct.setId(TEST_ID);
        Product result = productService.updateProduct(updateProduct);
        assertThat(result.getName(), is(TEST_UPDATE_NAME));
        assertThat(result.getBestBeforeTime(), is(TEST_UPDATE_TIME));
        assertThat(result.getPrice(), is(TEST_UPDATE_PRICE));
    }

    @Test
    void updateProduct2() {
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(this.product));

        Product result = productService.updateProduct(product);
        assertThat(result.getName(), is(TEST_NAME));
        assertThat(result.getBestBeforeTime(), is(TEST_TIME));
        assertThat(result.getPrice(), is(TEST_PRICE));
    }

    @Test
    void updateProductException1() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(product);
        });
    }

    @Test
    void updateProductException2() {
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(this.product));
        Product updateProduct = new Product(TEST_UPDATE_NAME, TEST_UPDATE_TIME, TEST_UPDATE_PRICE);
        updateProduct.setId(TEST_ID);
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(updateProduct);
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

    @Test
    void deleteProductException1() {
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(product));
        products.add(product);

        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(TEST_ID);
        });
    }

    @Test
    void deleteProductException2() {
        products.add(product);
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(TEST_ID);
        });
    }
}
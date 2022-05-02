package de.fhms.sweng.einkaufslistenverwaltung.model;

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

    private ProductService productService;
    private List<Product> products;
    private Product product;

    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "Apfel";
    private static final Integer TEST_TIME = 5;
    private static final Integer TEST_PRICE = 4;

    private static final Integer TEST_ID_FALSE = 2;

    @Test
    void getProductById() {
        given(this.productRepository.findById(TEST_ID)).willReturn(Optional.of(this.product));
        Product product = this.productService.getProductById(TEST_ID);
        assertThat(product.getId(), is(TEST_ID));
        assertThat(product.getName(), is(TEST_NAME));
        assertThat(product.getBestBeforeTime(), is(TEST_TIME));
        assertThat(product.getPrice(), is(TEST_PRICE));
    }
    @Test
    void getProductByIdException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.productService.getProductById(TEST_ID_FALSE);
        });
    }

    @Test
    void getAllProducts() {
        given(this.productRepository.getAll()).willReturn(Optional.of(this.products));
        products.add(this.product);

        List<Product> productList = productService.getAllProducts();
        assertFalse(productList.isEmpty());
    }
    @Test
    void getAllProductsException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            this.productService.getAllProducts();
        });
    }

    @Test
    void addProduct() {
        given(this.productRepository.getAll()).willReturn(Optional.of(this.products));

        assertFalse(this.productService.getAllProducts().contains(this.product));
        Product product = this.productService.addProduct(this.product);
        assertThat(product.getName(), is(TEST_NAME));
        assertThat(product.getBestBeforeTime(), is(TEST_TIME));
        assertThat(product.getPrice(), is(TEST_PRICE));
    }
    @Test
    void addProductException() {
        given(this.productRepository.getAll()).willReturn(Optional.of(this.products));
        products.add(this.product);

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            this.productService.addProduct(this.product);
        });
    }

    @Test
    void deleteProduct() {
        given(this.productRepository.findById(TEST_ID)).willReturn(Optional.of(this.product));
        given(this.productRepository.getAll()).willReturn(Optional.of(this.products));
        products.add(this.product);

        assertTrue(this.productService.getAllProducts().contains(this.product));
        this.productService.deleteProduct(TEST_ID);
        Mockito.verify(this.productRepository, times(1)).delete(this.product);
    }

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productRepository);
        this.products = new ArrayList<>();
        this.product = new Product(TEST_NAME, TEST_TIME, TEST_PRICE);
        this.product.setId(TEST_ID);
    }

}